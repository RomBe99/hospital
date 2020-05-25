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
import net.thumbtack.hospital.dtoresponse.patient.ticket.TicketDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.ticket.TicketToDoctorDtoResponse;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.Patient;
import net.thumbtack.hospital.model.TicketToDoctor;
import net.thumbtack.hospital.util.TicketToDoctorBuilder;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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
        Patient patient =
                new Patient(request.getLogin(), request.getPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(),
                        request.getEmail(), request.getAddress(), request.getPhone());

        patientDao.insertPatient(patient);

        return new PatientRegistrationDtoResponse(patient.getId(),
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }

    public EditPatientProfileDtoResponse editPatientProfile(String sessionId, EditPatientProfileDtoRequest request) throws PermissionDeniedException {
        int patientId = patientDao.hasPermissions(sessionId);

        Patient patient =
                new Patient(patientId, request.getNewPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(),
                        request.getEmail(), request.getAddress(), request.getPhone());

        patientDao.updatePatient(patient);

        return new EditPatientProfileDtoResponse(request.getFirstName(), request.getLastName(), request.getPatronymic(),
                request.getEmail(), request.getAddress(), request.getPhone(), request.getNewPassword());
    }

    public AppointmentToDoctorDtoResponse appointmentToDoctor(String sessionId, AppointmentToDoctorDtoRequest request) throws PermissionDeniedException {
        int patientId = patientDao.hasPermissions(sessionId);

        Doctor doctor = doctorDao.getDoctorById(request.getDoctorId());

        patientDao.appointmentToDoctor(patientId, request.getDoctorId(),
                LocalDate.parse(request.getDate()), LocalTime.parse(request.getTime()));

        LocalDate ticketDate = LocalDate.parse(request.getDate());
        LocalTime ticketTime = LocalTime.parse(request.getTime());

        TicketToDoctor ticket =
                new TicketToDoctor(TicketToDoctorBuilder.buildTicketTicketNumber(doctor.getId(), ticketDate, ticketTime),
                        ticketDate, ticketTime, doctor);

        return new AppointmentToDoctorDtoResponse(ticket.getNumber(), doctor.getId(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpecialty(), doctor.getCabinet(),
                request.getDate(), request.getTime());
    }

    public void denyMedicalCommission(String sessionId, int commissionTicketId) throws PermissionDeniedException {
        int patientId = patientDao.hasPermissions(sessionId);

        patientDao.denyMedicalCommission(patientId, commissionTicketId);
    }

    public void denyTicket(String sessionId, String ticketNumber) throws PermissionDeniedException {
        int patientId = patientDao.hasPermissions(sessionId);

        patientDao.denyTicket(patientId,
                TicketToDoctorBuilder.getDoctorIdFromTicketNumber(ticketNumber),
                TicketToDoctorBuilder.getDateFromTicketNumber(ticketNumber),
                TicketToDoctorBuilder.getTimeFromTicketNumber(ticketNumber));
    }

    public AllTicketsDtoResponse getTickets(String sessionId) throws PermissionDeniedException {
        int patientId = patientDao.hasPermissions(sessionId);

        List<TicketDtoResponse> allTickets = patientDao.getTicketsToDoctor(patientId).stream()
                .map(t -> new TicketToDoctorDtoResponse(
                        TicketToDoctorBuilder.buildTicketTicketNumber(t.getDoctor().getId(), t.getDate(), t.getTime()),
                        t.getDoctor().getCabinet(), t.getDate(), t.getTime(), t.getDoctor().getId(),
                        t.getDoctor().getFirstName(), t.getDoctor().getLastName(), t.getDoctor().getPatronymic(),
                        t.getDoctor().getSpecialty())).collect(Collectors.toList());
        // TODO Сделать для врачебной комиссии

        return new AllTicketsDtoResponse(allTickets);
    }
}