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
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.util.adapter.DtoAdapters;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.security.SecurityManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
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

    @Autowired
    public UserService(PatientDao patientDao, @Qualifier("UserDaoImpl") UserDao userDao,
                       AdministratorDao administratorDao, DoctorDao doctorDao, CommonDao commonDao, Constraints constraints) {
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.administratorDao = administratorDao;
        this.doctorDao = doctorDao;
        this.commonDao = commonDao;
        this.constraints = constraints;
    }

    public LoginUserDtoResponse login(LoginDtoRequest request, String sessionId) {
        final int userId = userDao.login(sessionId, request.getLogin(), request.getPassword());
        final UserType userType = UserType.valueOf(commonDao.getUserTypeByUserId(userId));

        final Map<UserType, Supplier<? extends LoginUserDtoResponse>> responseMap = new HashMap<>();
        responseMap.put(UserType.PATIENT, () -> {
            Patient p = patientDao.getPatientById(userId);

            return new PatientLoginDtoResponse(p.getId(), p.getFirstName(), p.getLastName(), p.getPatronymic(), p.getEmail(), p.getAddress(), p.getPhone());
        });
        responseMap.put(UserType.ADMINISTRATOR, () -> {
            Administrator a = administratorDao.getAdministratorById(userId);

            return new AdminLoginDtoResponse(a.getId(), a.getFirstName(), a.getLastName(), a.getPatronymic(), a.getPosition());
        });
        responseMap.put(UserType.DOCTOR, () -> {
            Doctor d = doctorDao.getDoctorById(userId);

            return new DoctorLoginDtoResponse(d.getId(), d.getFirstName(), d.getLastName(), d.getPatronymic(), d.getSpecialty(), d.getCabinet(),
                    d.getSchedule().stream()
                            .map(DtoAdapters::transform)
                            .collect(Collectors.toList()));
        });

        return responseMap.get(userType).get();
    }

    public EmptyDtoResponse logout(String sessionId) {
        userDao.logout(sessionId);

        return new EmptyDtoResponse();
    }

    public UserInformationDtoResponse getUserInformation(String sessionId) throws PermissionDeniedException {
        final int userId = SecurityManagerImpl
                .getSecurityManager()
                .hasPermission(sessionId);

        final UserType userType = UserType.valueOf(commonDao.getUserTypeByUserId(userId));

        final Map<UserType, Supplier<? extends UserInformationDtoResponse>> responseMap = new HashMap<>();
        responseMap.put(UserType.PATIENT, () -> {
            Patient patient = patientDao.getPatientById(userId);

            return new FullPatientInformationDtoResponse(patient.getId(),
                    patient.getLogin(), patient.getPassword(),
                    patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                    patient.getEmail(), patient.getAddress(), patient.getPhone());
        });
        responseMap.put(UserType.ADMINISTRATOR, () -> {
            Administrator admin = administratorDao.getAdministratorById(userId);

            return new AdminInformationDtoResponse(admin.getId(),
                    admin.getLogin(), admin.getPassword(),
                    admin.getFirstName(), admin.getLastName(), admin.getPatronymic(), admin.getPosition());
        });
        responseMap.put(UserType.DOCTOR, () -> {
            Doctor doctor = doctorDao.getDoctorById(userId);

            return new DoctorInformationDtoResponse(doctor.getId(),
                    doctor.getLogin(), doctor.getPassword(),
                    doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                    doctor.getSpecialty(), doctor.getCabinet(),
                    doctor.getSchedule().stream()
                            .map(DtoAdapters::transform)
                            .collect(Collectors.toList()));
        });

        return responseMap.get(userType).get();
    }

    public PatientInformationDtoResponse getPatientInformation(String sessionId, int patientId) throws PermissionDeniedException {
        SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT, UserType.DOCTOR, UserType.ADMINISTRATOR)
                .hasPermission(sessionId);

        final Patient patient = patientDao.getPatientById(patientId);

        return new PatientInformationDtoResponse(patientId,
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }

    public DoctorInformationDtoResponse getDoctorInformation(String sessionId, int doctorId,
                                                             LocalDate startDate, LocalDate endDate) throws PermissionDeniedException {
        final int patientId = SecurityManagerImpl
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
                        .map(DtoAdapters::transform)
                        .collect(Collectors.toList()));
    }

    public GetAllDoctorsDtoResponse getDoctorsInformation(String sessionId, String speciality,
                                                          LocalDate startDate, LocalDate endDate) throws PermissionDeniedException {
        final int patientId = SecurityManagerImpl
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
                                .map(DtoAdapters::transform)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList()));
    }

    public SettingsDtoResponse getSettings(String sessionId) {
        final Map<UserType, Supplier<? extends SettingsDtoResponse>> settingsSuppliers = new HashMap<>();
        settingsSuppliers.put(UserType.ADMINISTRATOR,
                () -> new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength()));
        settingsSuppliers.put(UserType.PATIENT,
                () -> new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength()));
        settingsSuppliers.put(UserType.DOCTOR,
                () -> new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength()));
        settingsSuppliers.put(null,
                () -> new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength()));

        for (UserType t : UserType.values()) {
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