package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
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

    @PostMapping(value = administratorRegistrationUrl)
    public RequestEntity<String> administratorRegistration(@CookieValue(value = "session.id") int sessionId,
                                                           @Valid @RequestBody AdminRegistrationDtoRequest request) {
        return null;
    }

    @PostMapping(value = doctorRegistrationUrl)
    public RequestEntity<String> doctorRegistration(@CookieValue(value = "session.id") int sessionId,
                                                    @Valid @RequestBody DoctorRegistrationDtoRequest request) {
        return null;
    }

    @PutMapping(value = editAdministratorProfileUrl)
    public RequestEntity<String> editAdministratorProfile(@CookieValue(value = "session.id") int sessionId,
                                                          @Valid @RequestBody EditAdminProfileDtoRequest request) {
        return null;
    }

    @PutMapping(value = editDoctorScheduleUrl)
    public RequestEntity<String> editDoctorSchedule(@PathVariable int doctorId,
                                                    @Valid @RequestBody EditDoctorScheduleDtoRequest request) {
        return null;
    }

    @DeleteMapping(value = removeDoctorUrl)
    public RequestEntity<String> removeDoctor(@CookieValue(value = "session.id") int sessionId,
                                              @PathVariable int doctorId,
                                              @Valid @RequestBody RemoveDoctorDtoRequest request) {
        return null;
    }
}