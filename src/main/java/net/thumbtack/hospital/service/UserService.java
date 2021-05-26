package net.thumbtack.hospital.service;

import net.thumbtack.hospital.configuration.Constraints;
import net.thumbtack.hospital.dao.*;
import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.AdminInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.AdminLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ServerSettingsDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.SettingsDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.FullPatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.user.GetAllDoctorsDtoResponse;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.util.adapter.DtoAdapters;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.security.SecurityManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service("UserService")
public class UserService {
    private final PatientDao patientDao;
    private final UserDao userDao;
    private final AdministratorDao administratorDao;
    private final DoctorDao doctorDao;
    private final CommonDao commonDao;
    private final Constraints constraints;
    private final DtoAdapters dtoAdapters;

    @Autowired
    public UserService(PatientDao patientDao, @Qualifier("UserDaoImpl") UserDao userDao,
                       AdministratorDao administratorDao, DoctorDao doctorDao, CommonDao commonDao, Constraints constraints, DtoAdapters dtoAdapters) {
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.administratorDao = administratorDao;
        this.doctorDao = doctorDao;
        this.commonDao = commonDao;
        this.constraints = constraints;
        this.dtoAdapters = dtoAdapters;
    }

    public LoginUserDtoResponse login(LoginDtoRequest request, String sessionId) {
        final var userId = userDao.login(sessionId, request.getLogin(), request.getPassword());
        final var userType = UserType.valueOf(commonDao.getUserTypeByUserId(userId));

        return Map.<UserType, Supplier<? extends LoginUserDtoResponse>>of(
                UserType.PATIENT, () -> {
                    final var patient = patientDao.getPatientById(userId);

                    return new PatientLoginDtoResponse(patient.getId(),
                            patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                            patient.getEmail(), patient.getAddress(), patient.getPhone());
                },
                UserType.ADMINISTRATOR, () -> {
                    final var administrator = administratorDao.getAdministratorById(userId);

                    return new AdminLoginDtoResponse(administrator.getId(),
                            administrator.getFirstName(), administrator.getLastName(), administrator.getPatronymic(),
                            administrator.getPosition());
                },
                UserType.DOCTOR, () -> {
                    final var doctor = doctorDao.getDoctorById(userId);

                    return new DoctorLoginDtoResponse(doctor.getId(),
                            doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                            doctor.getSpecialty(), doctor.getCabinet(),
                            doctor.getSchedule().stream()
                                    .map(dtoAdapters::transform)
                                    .collect(Collectors.toList()));
                })
                .get(userType)
                .get();
    }

    public EmptyDtoResponse logout(String sessionId) {
        userDao.logout(sessionId);

        return new EmptyDtoResponse();
    }

    public UserInformationDtoResponse getUserInformation(String sessionId) throws PermissionDeniedException {
        final var userId = SecurityManagerImpl
                .getSecurityManager()
                .hasPermission(sessionId);

        final var userType = UserType.valueOf(commonDao.getUserTypeByUserId(userId));

        return Map.<UserType, Supplier<? extends UserInformationDtoResponse>>of(
                UserType.PATIENT, () -> {
                    final var patient = patientDao.getPatientById(userId);

                    return new FullPatientInformationDtoResponse(patient.getId(),
                            patient.getLogin(), patient.getPassword(),
                            patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                            patient.getEmail(), patient.getAddress(), patient.getPhone());
                },
                UserType.ADMINISTRATOR, () -> {
                    final var admin = administratorDao.getAdministratorById(userId);

                    return new AdminInformationDtoResponse(admin.getId(),
                            admin.getLogin(), admin.getPassword(),
                            admin.getFirstName(), admin.getLastName(), admin.getPatronymic(), admin.getPosition());
                },
                UserType.DOCTOR, () -> {
                    final var doctor = doctorDao.getDoctorById(userId);

                    return new DoctorInformationDtoResponse(doctor.getId(),
                            doctor.getLogin(), doctor.getPassword(),
                            doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                            doctor.getSpecialty(), doctor.getCabinet(),
                            doctor.getSchedule().stream()
                                    .map(dtoAdapters::transform)
                                    .collect(Collectors.toList()));
                })
                .get(userType)
                .get();
    }

    public PatientInformationDtoResponse getPatientInformation(String sessionId, int patientId) throws PermissionDeniedException {
        SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT, UserType.DOCTOR, UserType.ADMINISTRATOR)
                .hasPermission(sessionId);

        final var patient = patientDao.getPatientById(patientId);

        return new PatientInformationDtoResponse(patientId,
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }

    public DoctorInformationDtoResponse getDoctorInformation(String sessionId, int doctorId,
                                                             LocalDate startDate, LocalDate endDate) throws PermissionDeniedException {
        final var patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);
        final Doctor doctor;

        if (startDate == null || endDate == null) {
            doctor = userDao.getDoctorInformationWithoutSchedule(doctorId);
        } else {
            doctor = userDao.getDoctorInformationWithSchedule(patientId, doctorId, startDate, endDate);
        }

        return doctor == null ? null : new DoctorInformationDtoResponse(doctor.getId(),
                doctor.getLogin(), doctor.getPassword(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                doctor.getSpecialty(), doctor.getCabinet(),
                doctor.getSchedule().stream()
                        .map(dtoAdapters::transform)
                        .collect(Collectors.toList()));
    }

    public GetAllDoctorsDtoResponse getDoctorsInformation(String sessionId, String speciality,
                                                          LocalDate startDate, LocalDate endDate) throws PermissionDeniedException {
        final var patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);
        final List<Doctor> doctors;

        if (startDate == null || endDate == null) {
            doctors = userDao.getDoctorsBySpecialityWithoutSchedule(speciality);
        } else {
            doctors = userDao.getDoctorsInformationWithSchedule(patientId, speciality, startDate, endDate);
        }

        return new GetAllDoctorsDtoResponse(doctors.stream()
                .map(d -> new DoctorInformationDtoResponse(d.getId(),
                        d.getLogin(), d.getPassword(),
                        d.getFirstName(), d.getLastName(), d.getPatronymic(),
                        d.getSpecialty(), d.getCabinet(),
                        d.getSchedule().stream()
                                .map(dtoAdapters::transform)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList()));
    }

    public SettingsDtoResponse getSettings(String sessionId) {
        final var settingsSuppliers = Map.<UserType, Supplier<? extends SettingsDtoResponse>>of(
                UserType.ADMINISTRATOR,
                () -> new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength()),
                UserType.PATIENT,
                () -> new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength()),
                UserType.DOCTOR,
                () -> new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength()),
                UserType.USER,
                () -> new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength())
        );

        for (var t : UserType.values()) {
            try {
                SecurityManagerImpl
                        .getSecurityManager(t)
                        .hasPermission(sessionId);

                return settingsSuppliers.get(t).get();
            } catch (PermissionDeniedException ignored) {
            }
        }

        return settingsSuppliers.get(null).get();
    }
}