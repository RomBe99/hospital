package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.model.Patient;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCodes;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService {
    private final PatientDao patientDao;
    private final UserDao userDao;

    @Autowired
    public UserService(PatientDao patientDao, @Qualifier("UserDaoImpl") UserDao userDao) {
        this.patientDao = patientDao;
        this.userDao = userDao;
    }

    // TODO Как узнать к какому типу LoginUserDtoResponse относится user?
    public LoginUserDtoResponse login(LoginDtoRequest request, String sessionId) {
        return null;
    }

    public EmptyDtoResponse logout(String sessionId) {
        userDao.logout(sessionId);

        return new EmptyDtoResponse();
    }

    // TODO Как узнать к какому типу UserInformationDtoResponse относится user?
    public UserInformationDtoResponse getUserInformation(String sessionId) {
        return null;
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
}