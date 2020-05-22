package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.patient.AppointmentToDoctorDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.EditPatientProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.ticket.AllTicketsDtoResponse;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.StringJoiner;

@Service("PatientService")
public class PatientService {
    private final PatientDao patientDao;
    private final DoctorDao doctorDao;

    @Autowired
    public PatientService(PatientDao patientDao, DoctorDao doctorDao) {
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
    }

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

    public EditPatientProfileDtoResponse editPatientProfile(String sessionId, EditPatientProfileDtoRequest request) {
        int patientId = patientDao.hasPermissions(sessionId);

        Patient p =
                new Patient(patientId, request.getNewPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(),
                        request.getEmail(), request.getAddress(), request.getPhone());

        patientDao.updatePatient(p);

        return new EditPatientProfileDtoResponse(request.getFirstName(), request.getLastName(), request.getPatronymic(),
                request.getEmail(), request.getAddress(), request.getPhone(), request.getNewPassword());
    }

    public AppointmentToDoctorDtoResponse appointmentToDoctor(String sessionId, AppointmentToDoctorDtoRequest request) {
        int patientId = patientDao.hasPermissions(sessionId);

        Doctor d = doctorDao.getDoctorById(request.getDoctorId());

        patientDao.appointmentToDoctor(patientId, request.getDoctorId(),
                LocalDate.parse(request.getDate()), LocalTime.parse(request.getTime()));

        String ticket = new StringJoiner("-")
                .add(String.valueOf(d.getId()))
                .add(request.getDate())
                .add(request.getTime())
                .toString();

        return new AppointmentToDoctorDtoResponse(ticket, d.getId(),
                d.getFirstName(), d.getLastName(), d.getPatronymic(), d.getSpecialty(), d.getCabinet(),
                request.getDate(), request.getTime());
    }

    public void denyMedicalCommission(String sessionId, int commissionTicketId) {
        int patientId = patientDao.hasPermissions(sessionId);

        patientDao.denyMedicalCommission(patientId, commissionTicketId);
    }

    public void denyTicket(String sessionId, int commissionTicketId) {
        int patientId = patientDao.hasPermissions(sessionId);

        patientDao.denyTicket(patientId, commissionTicketId);
    }

    public AllTicketsDtoResponse getTickets(String sessionId) {
        int patientId = patientDao.hasPermissions(sessionId);

        // TODO

        return new AllTicketsDtoResponse();
    }
}