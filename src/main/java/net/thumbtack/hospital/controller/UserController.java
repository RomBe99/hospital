package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.user.*;
import net.thumbtack.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    private static final String loginUrl = "/api/sessions";
    private static final String logoutUrl = "/api/sessions";
    private static final String getUserInformationUrl = "/api/account";
    private static final String getDoctorInformationUrl = "/api/doctors/{doctorId}";
    private static final String getDoctorsInformationUrl = "/api/doctors";
    private static final String getPatientInformationUrl = "/api/patients/{patientId}";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = loginUrl)
    public ResponseEntity<String> login(@Valid @RequestBody LoginDtoRequest request) {
        return null;
    }

    @DeleteMapping(value = logoutUrl)
    public RequestEntity<String> logout(@CookieValue(value = "session.id") int sessionId) {
        return null;
    }

    @GetMapping(value = getUserInformationUrl)
    public RequestEntity<String> getUserInformation(@CookieValue(value = "session.id") int sessionId) {
        return null;
    }

    @GetMapping(value = getDoctorInformationUrl)
    public RequestEntity<String> getDoctorInformation(@CookieValue(value = "session.id") int sessionId,
                                                      @PathVariable int doctorId,
                                                     @RequestParam(value = "schedule") String schedule,
                                                     @RequestParam(value = "startDate", required = false) String startDate,
                                                     @RequestParam(value = "endDate", required = false) String endDate) {
        return null;
    }

    @GetMapping(value = getDoctorsInformationUrl)
    public RequestEntity<String> getDoctorsInformation(@CookieValue(value = "session.id") int sessionId,
                                                       @RequestParam(value = "schedule") String schedule,
                                                       @RequestParam(value = "speciality", required = false) String speciality,
                                                       @RequestParam(value = "startDate", required = false) String startDate,
                                                       @RequestParam(value = "endDate", required = false) String endDate) {
        return null;
    }

    @GetMapping(value = getPatientInformationUrl)
    public RequestEntity<String> getPatientInformation(@CookieValue(value = "session.id") int sessionId,
                                                       @PathVariable int patientId) {
        return null;
    }
}