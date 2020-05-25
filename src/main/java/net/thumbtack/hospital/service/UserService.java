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
import net.thumbtack.hospital.mapper.UserTypes;
import net.thumbtack.hospital.model.Administrator;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.Patient;
import net.thumbtack.hospital.util.DtoAdapters;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCodes;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
        UserTypes userType = UserTypes.valueOf(commonDao.getUserTypeByUserId(userId));

        if (userType == UserTypes.PATIENT) {
            Patient patient = patientDao.getPatientById(userId);

            return new PatientLoginDtoResponse(patient.getId(),
                    patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                    patient.getEmail(), patient.getAddress(), patient.getPhone());
        }

        if (userType == UserTypes.ADMINISTRATOR) {
            Administrator admin = adminDao.getAdministratorById(userId);

            return new AdminLoginDtoResponse(admin.getId(),
                    admin.getFirstName(), admin.getLastName(), admin.getPatronymic(), admin.getPosition());
        }

        if (userType == UserTypes.DOCTOR) {
            Doctor doctor = doctorDao.getDoctorById(userId);

            return new DoctorLoginDtoResponse(doctor.getId(),
                    doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                    doctor.getSpecialty(), doctor.getCabinet(),
                    doctor.getSchedule().stream()
                            .map(DtoAdapters::scheduleCellToScheduleCellResponse)
                            .collect(Collectors.toList()));
        }

        throw new RuntimeException("Can't create login response");
    }

    public EmptyDtoResponse logout(String sessionId) {
        userDao.logout(sessionId);

        return new EmptyDtoResponse();
    }

    public UserInformationDtoResponse getUserInformation(String sessionId) throws PermissionDeniedException {
        int userId = userDao.hasPermissions(sessionId);
        UserTypes userType = UserTypes.valueOf(commonDao.getUserTypeByUserId(userId));

        userDao.logout(sessionId);

        if (userType == UserTypes.PATIENT) {
            Patient patient = patientDao.getPatientById(userId);

            return new FullPatientInformationDtoResponse(patient.getId(),
                    patient.getLogin(), patient.getPassword(),
                    patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                    patient.getEmail(), patient.getAddress(), patient.getPhone());
        }

        if (userType == UserTypes.ADMINISTRATOR) {
            Administrator admin = adminDao.getAdministratorById(userId);

            return new AdminInformationDtoResponse(admin.getId(),
                    admin.getLogin(), admin.getPassword(),
                    admin.getFirstName(), admin.getLastName(), admin.getPatronymic(), admin.getPosition());
        }

        if (userType == UserTypes.DOCTOR) {
            Doctor doctor = doctorDao.getDoctorById(userId);

            return new DoctorInformationDtoResponse(doctor.getId(),
                    doctor.getLogin(), doctor.getPassword(),
                    doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                    doctor.getSpecialty(), doctor.getCabinet(),
                    doctor.getSchedule().stream()
                            .map(DtoAdapters::scheduleCellToScheduleCellResponse)
                            .collect(Collectors.toList()));
        }

        throw new RuntimeException("Can't create user information response");
    }

    public PatientInformationDtoResponse getPatientInformation(String sessionId, int patientId) throws PermissionDeniedException {
        try {
            patientDao.hasPermissions(sessionId);

            throw new PermissionDeniedException(PermissionDeniedErrorCodes.PERMISSION_DENIED);
        } catch (RuntimeException ex) {
            Patient patient = patientDao.getPatientById(patientId);

            return new PatientInformationDtoResponse(patientId,
                    patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                    patient.getEmail(), patient.getAddress(), patient.getPhone());
        }
    }

    public DoctorInformationDtoResponse getDoctorInformation(String sessionId, int doctorId, String schedule, String startDate, String endDate) throws PermissionDeniedException {
        int patientId = patientDao.hasPermissions(sessionId);
        Doctor doctor;

        if (schedule != null && !schedule.isEmpty() && schedule.toLowerCase().equals("yes")) {
            doctor = userDao.getDoctorInformation(patientId, doctorId, LocalDate.parse(startDate), LocalDate.parse(endDate));
        } else {
            doctor = userDao.getDoctorInformation(patientId, doctorId, null, null);
        }

        return new DoctorInformationDtoResponse(doctor.getId(),
                doctor.getLogin(), doctor.getPassword(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                doctor.getSpecialty(), doctor.getCabinet(),
                doctor.getSchedule().stream()
                        .map(DtoAdapters::scheduleCellToScheduleCellResponse)
                        .collect(Collectors.toList()));
    }

    public GetAllDoctorsDtoResponse getDoctorsInformation(String sessionId, String schedule, String speciality, String startDate, String endDate) throws PermissionDeniedException {
        int patientId = patientDao.hasPermissions(sessionId);
        List<Doctor> doctors;

        if (schedule != null && !schedule.isEmpty() && schedule.toLowerCase().equals("yes")) {
            doctors = userDao.getDoctorsInformation(patientId, speciality, LocalDate.parse(startDate), LocalDate.parse(endDate));
        } else {
            doctors = userDao.getDoctorsInformation(patientId, speciality, null, null);
        }

        return new GetAllDoctorsDtoResponse(doctors.stream()
                .map(d -> new DoctorInformationDtoResponse(d.getId(),
                        d.getLogin(), d.getPassword(),
                        d.getFirstName(), d.getLastName(), d.getPatronymic(),
                        d.getSpecialty(), d.getCabinet(),
                        d.getSchedule().stream()
                                .map(DtoAdapters::scheduleCellToScheduleCellResponse)
                                .collect(Collectors.toList()))).collect(Collectors.toList()));
    }
}