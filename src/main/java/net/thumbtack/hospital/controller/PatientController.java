package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PatientController {
    private static final String patientRegistrationUrl = "/api/patients";
    private static final String editPatientProfileUrl = "/api/patients";
    private static final String appointmentToDoctorUrl = "/api/tickets";
    private static final String denyMedicalCommissionUrl = "/api/commissions/{commissionTicketId}";
    private static final String getTicketsUrl = "/api/tickets";
    private static final String denyTicketUrl = "/api/tickets";

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(value = patientRegistrationUrl)
    public RequestEntity<String> patientRegistration(@Valid @RequestBody PatientRegistrationDtoRequest request) {
        return null;
    }

    @PutMapping(value = editPatientProfileUrl)
    public RequestEntity<String> editPatientProfile(@CookieValue(value = "session.id") int sessionId,
                                                    @Valid @RequestBody EditPatientProfileDtoRequest request) {
        return null;
    }

    @PatchMapping(value = appointmentToDoctorUrl)
    public RequestEntity<String> appointmentToDoctor(@CookieValue(value = "session.id") int sessionId,
                                                     @Valid @RequestBody AppointmentToDoctorDtoRequest request) {
        return null;
    }

    @PatchMapping(value = denyMedicalCommissionUrl)
    public RequestEntity<String> denyMedicalCommission(@CookieValue(value = "session.id") int sessionId,
                                                       @PathVariable int commissionTicketId) {
        return null;
    }

    @DeleteMapping(value = denyTicketUrl)
    public RequestEntity<String> denyTicket(@CookieValue(value = "session.id") int sessionId) {
        return null;
    }

    @GetMapping(value = getTicketsUrl)
    public RequestEntity<String> getTickets(@CookieValue(value = "session.id") int sessionId) {
        return null;
    }
}