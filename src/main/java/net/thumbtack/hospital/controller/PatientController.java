package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class PatientController {
    private static final String patientRegistrationUrl = "patients";
    private static final String editPatientProfileUrl = "patients";
    private static final String appointmentToDoctorUrl = "tickets";
    private static final String denyMedicalCommissionUrl = "commissions/{commissionTicketId}";
    private static final String getTicketsUrl = "tickets";
    private static final String denyTicketUrl = "tickets";

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(value = patientRegistrationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> patientRegistration(@Valid @RequestBody PatientRegistrationDtoRequest request) {
        return null;
    }

    @PutMapping(value = editPatientProfileUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editPatientProfile(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                                     @Valid @RequestBody EditPatientProfileDtoRequest request) {
        return null;
    }

    @PatchMapping(value = appointmentToDoctorUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> appointmentToDoctor(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                                      @Valid @RequestBody AppointmentToDoctorDtoRequest request) {
        return null;
    }

    @PatchMapping(value = denyMedicalCommissionUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> denyMedicalCommission(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                                        @PathVariable int commissionTicketId) {
        return null;
    }

    @DeleteMapping(value = denyTicketUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> denyTicket(@CookieValue(value = "JAVASESSIONID") int sessionId) {
        return null;
    }

    @GetMapping(value = getTicketsUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTickets(@CookieValue(value = "JAVASESSIONID") int sessionId) {
        return null;
    }
}