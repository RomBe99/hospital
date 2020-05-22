package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.user.GetAllDoctorsDtoResponse;
import net.thumbtack.hospital.service.UserService;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController("UserController")
@RequestMapping("/api")
public class UserController {
    public static final String loginUrl = "sessions";
    public static final String logoutUrl = "sessions";
    public static final String getUserInformationUrl = "account";
    public static final String getDoctorInformationUrl = "doctors/{doctorId}";
    public static final String getDoctorsInformationUrl = "doctors";
    public static final String getPatientInformationUrl = "patients/{patientId}";

    private final UserService userService;
    private final CookieFactory cookieFactory;

    @Autowired
    public UserController(UserService userService, CookieFactory cookieFactory) {
        this.userService = userService;
        this.cookieFactory = cookieFactory;
    }

    @PostMapping(value = loginUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LoginUserDtoResponse login(@Valid @RequestBody LoginDtoRequest request, HttpServletResponse response) {
        Cookie javaSessionId = cookieFactory.getCookieByCookieName(CookieFactory.JAVA_SESSION_ID);

        LoginUserDtoResponse dtoResponse = userService.login(request, javaSessionId.getValue());
        response.addCookie(javaSessionId);

        return dtoResponse;
    }

    @DeleteMapping(value = logoutUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EmptyDtoResponse logout(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId) {
        return userService.logout(sessionId);
    }

    @GetMapping(value = getUserInformationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserInformationDtoResponse getUserInformation(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId) {
        return userService.getUserInformation(sessionId);
    }

    @GetMapping(value = getDoctorInformationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DoctorInformationDtoResponse getDoctorInformation(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                             @PathVariable int doctorId,
                                                             @RequestParam(value = "schedule") String schedule,
                                                             @RequestParam(value = "startDate", required = false) String startDate,
                                                             @RequestParam(value = "endDate", required = false) String endDate) {
        // TODO
        return null;
    }

    @GetMapping(value = getDoctorsInformationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GetAllDoctorsDtoResponse getDoctorsInformation(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                          @RequestParam(value = "schedule") String schedule,
                                                          @RequestParam(value = "speciality", required = false) String speciality,
                                                          @RequestParam(value = "startDate", required = false) String startDate,
                                                          @RequestParam(value = "endDate", required = false) String endDate) {
        // TODO
        return null;
    }

    @GetMapping(value = getPatientInformationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PatientInformationDtoResponse getPatientInformation(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                               @PathVariable int patientId) {
        return userService.getPatientInformation(sessionId, patientId);
    }
}