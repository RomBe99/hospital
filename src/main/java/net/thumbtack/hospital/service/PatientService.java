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
import net.thumbtack.hospital.dtoresponse.patient.ticket.TicketToMedicalCommissionDtoResponse;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.security.manager.SecurityManagerImpl;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("PatientService")
public class PatientService {
    private final PatientDao patientDao;
    private final DoctorDao doctorDao;

    public static String phoneTransformer(String phone) {
        String emptyStr = "";
        String clearPhoneNumberRegexp = "\\D+";

        if (phone.startsWith("7")) {
            return '+' + phone.replaceAll(clearPhoneNumberRegexp, emptyStr);
        }

        return phone.replaceAll(clearPhoneNumberRegexp, emptyStr);
    }

    @Autowired
    public PatientService(PatientDao patientDao, DoctorDao doctorDao) {
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
    }

    public PatientRegistrationDtoResponse patientRegistration(PatientRegistrationDtoRequest request) {
        request.setPhone(phoneTransformer(request.getPhone()));

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
        int patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);

        request.setPhone(phoneTransformer(request.getPhone()));

        Patient patient =
                new Patient(patientId, null, request.getNewPassword(),
                        request.getFirstName(), request.getLastName(), request.getPatronymic(),
                        request.getEmail(), request.getAddress(), request.getPhone());

        patientDao.updatePatient(patient);

        return new EditPatientProfileDtoResponse(request.getFirstName(), request.getLastName(), request.getPatronymic(),
                request.getEmail(), request.getAddress(), request.getPhone(), request.getNewPassword());
    }

    public AppointmentToDoctorDtoResponse appointmentToDoctor(String sessionId, AppointmentToDoctorDtoRequest request) throws PermissionDeniedException {
        int patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);

        Doctor doctor = doctorDao.getDoctorById(request.getDoctorId());

        LocalDate ticketDate = LocalDate.parse(request.getDate());
        LocalTime ticketTime = LocalTime.parse(request.getTime());

        patientDao.appointmentToDoctor(patientId, request.getDoctorId(), ticketDate, ticketTime);

        return new AppointmentToDoctorDtoResponse(TicketFactory.buildTicketToDoctor(request.getDoctorId(), ticketDate, ticketTime),
                request.getDoctorId(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpecialty(), doctor.getCabinet(),
                ticketDate.toString(), ticketTime.toString());
    }

    public void denyMedicalCommission(String sessionId, String ticket) throws PermissionDeniedException {
        SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);

        patientDao.denyMedicalCommission(ticket);
    }

    public void denyTicket(String sessionId, String ticket) throws PermissionDeniedException {
        patientDao.hasPermissions(sessionId);

        patientDao.denyTicket(ticket);
    }

    public AllTicketsDtoResponse getTickets(String sessionId) throws PermissionDeniedException {
        int patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);

        List<TicketToDoctor> ticketsToDoctors = patientDao.getTicketsToDoctor(patientId);
        List<TicketToMedicalCommission> ticketsToMedicalCommission = patientDao.getTicketsToMedicalCommission(patientId);

        List<TicketDtoResponse> tickets = new ArrayList<>(ticketsToDoctors.size() + ticketsToMedicalCommission.size());

        tickets.addAll(ticketsToDoctors.stream()
                .map(t -> new TicketToDoctorDtoResponse(
                        t.getTicket(), t.getRoom(), t.getDate(), t.getTime(), t.getDoctorId(),
                        t.getDoctorFirstName(), t.getDoctorLastName(), t.getDoctorPatronymic(),
                        t.getSpeciality()))
                .collect(Collectors.toList()));

        tickets.addAll(ticketsToMedicalCommission.stream()
                .map(t -> new TicketToMedicalCommissionDtoResponse(
                        t.getTicket(), t.getRoom(), t.getDate(), t.getTime(),
                        t.getPatientId(), t.getDoctorIds(), t.getDuration()))
                .collect(Collectors.toList()));

        return new AllTicketsDtoResponse(tickets);
    }
}