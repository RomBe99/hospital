package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService {
    private final PatientDao patientDao;
    private final UserDao userDao;

    @Autowired
    public UserService(PatientDao patientDao, @Qualifier("userDaoImpl") UserDao userDao) {
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

    // Вынести в другой сервис объеденяющий в себе полномочия администраторов и докторов?
    // TODO Что в данном случае делать с sessionId?
    public PatientInformationDtoResponse getPatientInformation(String sessionId, int patientId) {
        Patient p = patientDao.getPatientById(patientId);

        return new PatientInformationDtoResponse(patientId,
                p.getFirstName(), p.getLastName(), p.getPatronymic(),
                p.getEmail(), p.getAddress(), p.getPhone());
    }
}