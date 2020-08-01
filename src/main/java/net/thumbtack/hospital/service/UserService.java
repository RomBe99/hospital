package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.*;
import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.AdminInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.AdminLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.FullPatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.user.GetAllDoctorsDtoResponse;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.util.DtoAdapters;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.security.manager.SecurityManagerImpl;
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
    private final AdminDao adminDao;
    private final DoctorDao doctorDao;
    private final CommonDao commonDao;

    @Autowired
    public UserService(PatientDao patientDao, @Qualifier("UserDaoImpl") UserDao userDao, AdminDao adminDao, DoctorDao doctorDao, CommonDao commonDao) {
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.adminDao = adminDao;
        this.doctorDao = doctorDao;
        this.commonDao = commonDao;
    }

    public LoginUserDtoResponse login(LoginDtoRequest request, String sessionId) {
        int userId = userDao.login(sessionId, request.getLogin(), request.getPassword());
        UserType userType = UserType.valueOf(commonDao.getUserTypeByUserId(userId));

        Map<UserType, Supplier<? extends LoginUserDtoResponse>> responseMap = new HashMap<>();
        responseMap.put(UserType.PATIENT, () -> {
            Patient p = patientDao.getPatientById(userId);

            return new PatientLoginDtoResponse(p.getId(), p.getFirstName(), p.getLastName(), p.getPatronymic(), p.getEmail(), p.getAddress(), p.getPhone());
        });
        responseMap.put(UserType.ADMINISTRATOR, () -> {
            Administrator a = adminDao.getAdministratorById(userId);

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
        int userId = SecurityManagerImpl
                .getSecurityManager()
                .hasPermission(sessionId);
        UserType userType = UserType.valueOf(commonDao.getUserTypeByUserId(userId));

        Map<UserType, Supplier<? extends UserInformationDtoResponse>> responseMap = new HashMap<>();
        responseMap.put(UserType.PATIENT, () -> {
            Patient patient = patientDao.getPatientById(userId);

            return new FullPatientInformationDtoResponse(patient.getId(),
                    patient.getLogin(), patient.getPassword(),
                    patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                    patient.getEmail(), patient.getAddress(), patient.getPhone());
        });
        responseMap.put(UserType.ADMINISTRATOR, () -> {
            Administrator admin = adminDao.getAdministratorById(userId);

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

        Patient patient = patientDao.getPatientById(patientId);

        return new PatientInformationDtoResponse(patientId,
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }

    public DoctorInformationDtoResponse getDoctorInformation(String sessionId, int doctorId, LocalDate startDate, LocalDate endDate) throws PermissionDeniedException {
        int patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);
        Doctor doctor = userDao.getDoctorInformation(patientId, doctorId, startDate, endDate);

        return new DoctorInformationDtoResponse(doctor.getId(),
                doctor.getLogin(), doctor.getPassword(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                doctor.getSpecialty(), doctor.getCabinet(),
                doctor.getSchedule().stream()
                        .map(DtoAdapters::transform)
                        .collect(Collectors.toList()));
    }

    public GetAllDoctorsDtoResponse getDoctorsInformation(String sessionId, String speciality, LocalDate startDate, LocalDate endDate) throws PermissionDeniedException {
        int patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);
        List<Doctor> doctors = userDao.getDoctorsInformation(patientId, speciality, startDate, endDate);

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
}