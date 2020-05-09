package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final String loginUrl = "sessions";
    private static final String logoutUrl = "sessions";
    private static final String getUserInformationUrl = "account";
    private static final String getDoctorInformationUrl = "doctors/{doctorId}";
    private static final String getDoctorsInformationUrl = "doctors";
    private static final String getPatientInformationUrl = "patients/{patientId}";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = loginUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@Valid @RequestBody LoginDtoRequest request) {
        return null;
    }

    @DeleteMapping(value = logoutUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout(@CookieValue(value = "JAVASESSIONID") int sessionId) {
        return null;
    }

    @GetMapping(value = getUserInformationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserInformation(@CookieValue(value = "JAVASESSIONID") int sessionId) {
        return null;
    }

    @GetMapping(value = getDoctorInformationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDoctorInformation(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                                      @PathVariable int doctorId,
                                                     @RequestParam(value = "schedule") String schedule,
                                                     @RequestParam(value = "startDate", required = false) String startDate,
                                                     @RequestParam(value = "endDate", required = false) String endDate) {
        return null;
    }

    @GetMapping(value = getDoctorsInformationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDoctorsInformation(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                                       @RequestParam(value = "schedule") String schedule,
                                                       @RequestParam(value = "speciality", required = false) String speciality,
                                                       @RequestParam(value = "startDate", required = false) String startDate,
                                                       @RequestParam(value = "endDate", required = false) String endDate) {
        return null;
    }

    @GetMapping(value = getPatientInformationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPatientInformation(@CookieValue(value = "JAVASESSIONID") int sessionId,
                                                       @PathVariable int patientId) {
        return null;
    }
}