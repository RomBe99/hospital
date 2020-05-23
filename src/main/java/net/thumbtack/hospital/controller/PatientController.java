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
    public static final String PATIENT_REGISTRATION_URL = "patients";
    public static final String EDIT_PATIENT_PROFILE_URL = "patients";
    public static final String APPOINTMENT_TO_DOCTOR_URL = "tickets";
    public static final String DENY_MEDICAL_COMMISSION_URL = "commissions/{commissionTicketId}";
    public static final String GET_TICKETS_URL = "tickets";
    public static final String DENY_TICKET_URL = "tickets/{ticketNumber}";

    private final PatientService patientService;
    private final UserService userService;
    private final CookieFactory cookieFactory;

    @Autowired
    public PatientController(PatientService patientService, UserService userService, CookieFactory cookieFactory) {
        this.patientService = patientService;
        this.userService = userService;
        this.cookieFactory = cookieFactory;
    }

    @PostMapping(value = PATIENT_REGISTRATION_URL,
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

    @PutMapping(value = EDIT_PATIENT_PROFILE_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EditPatientProfileDtoResponse editPatientProfile(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                            @Valid @RequestBody EditPatientProfileDtoRequest request) {
        return patientService.editPatientProfile(sessionId, request);
    }

    @PatchMapping(value = APPOINTMENT_TO_DOCTOR_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AppointmentToDoctorDtoResponse appointmentToDoctor(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                              @Valid @RequestBody AppointmentToDoctorDtoRequest request) {
        return patientService.appointmentToDoctor(sessionId, request);
    }

    @PatchMapping(value = DENY_MEDICAL_COMMISSION_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EmptyDtoResponse denyMedicalCommission(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                  @PathVariable int commissionTicketId) {
        patientService.denyMedicalCommission(sessionId, commissionTicketId);

        return new EmptyDtoResponse();
    }

    @DeleteMapping(value = DENY_TICKET_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EmptyDtoResponse denyTicket(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                       @PathVariable String ticketNumber) {
        int scheduleCellId = Integer.parseInt(ticketNumber.split("-")[0]);

        patientService.denyTicket(sessionId, scheduleCellId);

        return new EmptyDtoResponse();
    }

    @GetMapping(value = GET_TICKETS_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AllTicketsDtoResponse getTickets(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId) {
        return patientService.getTickets(sessionId);
    }
}