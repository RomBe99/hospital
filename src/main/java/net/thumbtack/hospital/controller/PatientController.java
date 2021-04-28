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
import net.thumbtack.hospital.util.error.DoctorNotFoundException;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.error.ScheduleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController("PatientController")
@RequestMapping(PatientController.PREFIX_URL)
public class PatientController {
    public static final String PREFIX_URL = "/api";
    public static final String PATIENT_REGISTRATION_URL = "patients";
    public static final String EDIT_PATIENT_PROFILE_URL = "patients";
    public static final String APPOINTMENT_TO_DOCTOR_URL = "tickets";
    public static final String DENY_MEDICAL_COMMISSION_URL = "commissions/{ticketTitle}";
    public static final String GET_TICKETS_URL = "tickets";
    public static final String DENY_TICKET_TO_DOCTOR_URL = "tickets/{ticketTitle}";

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
        final var dtoResponse = patientService.patientRegistration(request);

        final var javaSessionId = cookieFactory.produceCookie(CookieFactory.JAVA_SESSION_ID);

        userService.login(new LoginDtoRequest(request.getLogin(), request.getPassword()), javaSessionId.getValue());
        response.addCookie(javaSessionId);

        return dtoResponse;
    }

    @PutMapping(value = EDIT_PATIENT_PROFILE_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EditPatientProfileDtoResponse editPatientProfile(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                            @Valid @RequestBody EditPatientProfileDtoRequest request) throws PermissionDeniedException {
        return patientService.editPatientProfile(sessionId, request);
    }

    @PatchMapping(value = APPOINTMENT_TO_DOCTOR_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AppointmentToDoctorDtoResponse appointmentToDoctor(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                              @Valid @RequestBody AppointmentToDoctorDtoRequest request)
            throws PermissionDeniedException, ScheduleException, DoctorNotFoundException {
        return patientService.appointmentToDoctor(sessionId, request);
    }

    @PatchMapping(value = DENY_MEDICAL_COMMISSION_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EmptyDtoResponse denyMedicalCommission(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                  @PathVariable String ticketTitle) throws PermissionDeniedException {
        patientService.denyMedicalCommission(sessionId, ticketTitle);

        return new EmptyDtoResponse();
    }

    @DeleteMapping(value = DENY_TICKET_TO_DOCTOR_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EmptyDtoResponse denyTicket(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                       @PathVariable String ticketTitle) throws PermissionDeniedException {
        patientService.denyTicket(sessionId, ticketTitle);

        return new EmptyDtoResponse();
    }

    @GetMapping(value = GET_TICKETS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AllTicketsDtoResponse getTickets(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId) throws PermissionDeniedException {
        return patientService.getTickets(sessionId);
    }
}