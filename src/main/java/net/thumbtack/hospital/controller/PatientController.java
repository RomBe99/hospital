package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.AppointmentToDoctorDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.EditPatientProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.ticket.AllTicketsDtoResponse;
import net.thumbtack.hospital.service.PatientService;
import net.thumbtack.hospital.service.UserService;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController("PatientController")
@RequestMapping("/api")
public class PatientController {
    public static final String patientRegistrationUrl = "patients";
    public static final String editPatientProfileUrl = "patients";
    public static final String appointmentToDoctorUrl = "tickets";
    public static final String denyMedicalCommissionUrl = "commissions/{commissionTicketId}";
    public static final String getTicketsUrl = "tickets";
    public static final String denyTicketUrl = "tickets/{ticketNumber}";

    private final PatientService patientService;
    private final UserService userService;
    private final CookieFactory cookieFactory;

    @Autowired
    public PatientController(PatientService patientService, UserService userService, CookieFactory cookieFactory) {
        this.patientService = patientService;
        this.userService = userService;
        this.cookieFactory = cookieFactory;
    }

    @PostMapping(value = patientRegistrationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PatientRegistrationDtoResponse patientRegistration(@Valid @RequestBody PatientRegistrationDtoRequest request,
                                                              HttpServletResponse response) {
        PatientRegistrationDtoResponse dtoResponse = patientService.patientRegistration(request);

        Cookie javaSessionId = cookieFactory.getCookieByCookieName(CookieFactory.JAVA_SESSION_ID);

        userService.login(new LoginDtoRequest(request.getLogin(), request.getPassword()), javaSessionId.getValue());
        response.addCookie(javaSessionId);

        return dtoResponse;
    }

    @PutMapping(value = editPatientProfileUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EditPatientProfileDtoResponse editPatientProfile(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                            @Valid @RequestBody EditPatientProfileDtoRequest request) {
        return patientService.editPatientProfile(sessionId, request);
    }

    @PatchMapping(value = appointmentToDoctorUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AppointmentToDoctorDtoResponse appointmentToDoctor(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                              @Valid @RequestBody AppointmentToDoctorDtoRequest request) {
        return patientService.appointmentToDoctor(sessionId, request);
    }

    @PatchMapping(value = denyMedicalCommissionUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EmptyDtoResponse denyMedicalCommission(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                  @PathVariable int commissionTicketId) {
        patientService.denyMedicalCommission(sessionId, commissionTicketId);

        return new EmptyDtoResponse();
    }

    @DeleteMapping(value = denyTicketUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EmptyDtoResponse denyTicket(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                       @PathVariable String ticketNumber) {
        int scheduleCellId = Integer.parseInt(ticketNumber.split("-")[0]);

        patientService.denyTicket(sessionId, scheduleCellId);

        return new EmptyDtoResponse();
    }

    @GetMapping(value = getTicketsUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AllTicketsDtoResponse getTickets(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId) {
        return patientService.getTickets(sessionId);
    }
}