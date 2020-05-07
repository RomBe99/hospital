package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService {
    private final UserDao userDao;
    private final PatientDao patientDao;
    private final DoctorDao doctorDao;

    @Autowired
    public UserService(UserDao userDao, PatientDao patientDao, DoctorDao doctorDao) {
        this.userDao = userDao;
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
    }

    public PatientInformationDtoResponse getPatientInformation(int patientId) {
        Patient p = patientDao.getPatientById(patientId);

        return new PatientInformationDtoResponse(patientId,
                p.getFirstName(), p.getLastName(), p.getPatronymic(),
                p.getEmail(), p.getAddress(), p.getPhone());
    }
}