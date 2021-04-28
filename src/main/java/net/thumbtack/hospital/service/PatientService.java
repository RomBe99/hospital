package net.thumbtack.hospital.service;

import net.thumbtack.hospital.dao.*;
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
import net.thumbtack.hospital.util.error.*;
import net.thumbtack.hospital.util.security.SecurityManagerImpl;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service("PatientService")
public class PatientService {
    private final DoctorDao doctorDao;
    private final CommonDao commonDao;
    private final PatientDao patientDao;
    private final ScheduleDao scheduleDao;
    private final MedicalCommissionDao medicalCommissionDao;

    public static String phoneTransformer(String phone) {
        final var result = phone.replaceAll("\\D+", "");

        return result.startsWith("7") ? '+' + result : result;
    }

    @Autowired
    public PatientService(PatientDao patientDao, DoctorDao doctorDao, CommonDao commonDao, ScheduleDao scheduleDao, MedicalCommissionDao medicalCommissionDao) {
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
        this.commonDao = commonDao;
        this.scheduleDao = scheduleDao;
        this.medicalCommissionDao = medicalCommissionDao;
    }

    public PatientRegistrationDtoResponse patientRegistration(PatientRegistrationDtoRequest request) {
        request.setPhone(phoneTransformer(request.getPhone()));

        final var patient = new Patient(request.getLogin(), request.getPassword(),
                request.getFirstName(), request.getLastName(), request.getPatronymic(),
                request.getEmail(), request.getAddress(), request.getPhone());

        patientDao.insertPatient(patient);

        return new PatientRegistrationDtoResponse(patient.getId(),
                patient.getFirstName(), patient.getLastName(), patient.getPatronymic(),
                patient.getEmail(), patient.getAddress(), patient.getPhone());
    }

    public EditPatientProfileDtoResponse editPatientProfile(String sessionId, EditPatientProfileDtoRequest request) throws PermissionDeniedException {
        final var patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);

        request.setPhone(phoneTransformer(request.getPhone()));

        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            request.setNewPassword(request.getOldPassword());
        }

        final var patient = new Patient(patientId, null, request.getOldPassword(),
                request.getFirstName(), request.getLastName(), request.getPatronymic(),
                request.getEmail(), request.getAddress(), request.getPhone());

        patientDao.updatePatient(patient, request.getNewPassword());

        return new EditPatientProfileDtoResponse(request.getFirstName(), request.getLastName(), request.getPatronymic(),
                request.getEmail(), request.getAddress(), request.getPhone(), request.getNewPassword());
    }

    public AppointmentToDoctorDtoResponse appointmentToDoctor(String sessionId, AppointmentToDoctorDtoRequest request)
            throws PermissionDeniedException, ScheduleException, DoctorNotFoundException {
        final var patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);

        final Doctor doctor;

        if (request.getDoctorId() != 0) {
            doctor = doctorDao.getDoctorById(request.getDoctorId());
        } else {
            doctor = doctorDao.getRandomDoctorBySpeciality(request.getSpeciality());
        }

        if (doctor == null) {
            throw new DoctorNotFoundException(DoctorNotFoundErrorCode.DOCTOR_NOT_FOUND);
        }

        final var ticketDate = LocalDate.parse(request.getDate());
        final var ticketTime = LocalTime.parse(request.getTime());
        final var ticketTitle = TicketFactory.buildTicketToDoctor(doctor.getId(), ticketDate, ticketTime);

        final var containsAppointment = commonDao.containsAppointment(ticketTitle);

        if (containsAppointment) {
            throw new ScheduleException(ScheduleErrorCode.ALREADY_CONTAINS_APPOINTMENT);
        }

        scheduleDao.appointmentToDoctor(patientId, ticketTitle);

        return new AppointmentToDoctorDtoResponse(ticketTitle, doctor.getId(),
                doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpecialty(), doctor.getCabinet(),
                ticketDate.toString(), ticketTime.toString());
    }

    public void denyMedicalCommission(String sessionId, String ticketTitle) throws PermissionDeniedException {
        SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);

        medicalCommissionDao.denyMedicalCommission(ticketTitle);
    }

    public void denyTicket(String sessionId, String ticketTitle) throws PermissionDeniedException {
        final var patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);

        scheduleDao.denyTicket(patientId, ticketTitle);
    }

    public AllTicketsDtoResponse getTickets(String sessionId) throws PermissionDeniedException {
        final var patientId = SecurityManagerImpl
                .getSecurityManager(UserType.PATIENT)
                .hasPermission(sessionId);

        final var ticketsToDoctors = scheduleDao.getTicketsToDoctor(patientId);
        final var ticketsToMedicalCommission = medicalCommissionDao.getTicketsToMedicalCommission(patientId);

        final var tickets = new ArrayList<TicketDtoResponse>(ticketsToDoctors.size() + ticketsToMedicalCommission.size());

        tickets.addAll(ticketsToDoctors.stream()
                .map(t -> new TicketToDoctorDtoResponse(
                        t.getTitle(), t.getRoom(), t.getDate(), t.getTime(), t.getDoctorId(),
                        t.getDoctorFirstName(), t.getDoctorLastName(), t.getDoctorPatronymic(),
                        t.getSpeciality()))
                .collect(Collectors.toList()));

        tickets.addAll(ticketsToMedicalCommission.stream()
                .map(t -> new TicketToMedicalCommissionDtoResponse(
                        t.getTitle(), t.getRoom(), t.getDate(), t.getTime(),
                        t.getPatientId(), t.getDoctorIds(), t.getDuration()))
                .collect(Collectors.toList()));

        return new AllTicketsDtoResponse(tickets);
    }
}