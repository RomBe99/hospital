package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.patient.EditPatientProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.model.Patient;
import org.springframework.stereotype.Service;

@Service("PatientService")
public class PatientService {
    private PatientDao patientDao;

    public PatientRegistrationDtoResponse patientRegistration(PatientRegistrationDtoRequest request) {
        Patient p =
                new Patient(request.getLogin(), request.getPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(),
                        request.getEmail(), request.getAddress(), request.getPhone());

        patientDao.insertPatient(p);

        return new PatientRegistrationDtoResponse(p.getId(),
                p.getFirstName(), p.getLastName(), p.getPatronymic(),
                p.getEmail(), p.getAddress(), p.getPhone());
    }

    public EditPatientProfileDtoResponse editPatientProfile(int patientId, EditPatientProfileDtoRequest request) {
        Patient p =
                new Patient(patientId, request.getNewPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(),
                        request.getEmail(), request.getAddress(), request.getPhone());

        patientDao.updatePatient(p);

        return new EditPatientProfileDtoResponse(request.getFirstName(), request.getLastName(), request.getPatronymic(),
                request.getEmail(), request.getAddress(), request.getPhone(), request.getNewPassword());
    }
}