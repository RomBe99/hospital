package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AdministratorController {
    private static final String administratorRegistrationUrl = "admins";
    private static final String doctorRegistrationUrl = "doctors";
    private static final String editAdministratorProfileUrl = "admins";
    private static final String editDoctorScheduleUrl = "doctors/{doctorId}";
    private static final String removeDoctorUrl = "doctors/{doctorId}";

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping(value = administratorRegistrationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> administratorRegistration(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                                            @Valid @RequestBody AdminRegistrationDtoRequest request) {
        return null;
    }

    @PostMapping(value = doctorRegistrationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doctorRegistration(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                                     @Valid @RequestBody DoctorRegistrationDtoRequest request) {
        return null;
    }

    @PutMapping(value = editAdministratorProfileUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editAdministratorProfile(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                                           @Valid @RequestBody EditAdminProfileDtoRequest request) {
        return null;
    }

    @PutMapping(value = editDoctorScheduleUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editDoctorSchedule(@PathVariable int doctorId,
                                                     @Valid @RequestBody EditDoctorScheduleDtoRequest request) {
        return null;
    }

    @DeleteMapping(value = removeDoctorUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeDoctor(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                               @PathVariable int doctorId,
                                               @Valid @RequestBody RemoveDoctorDtoRequest request) {
        return null;
    }
}