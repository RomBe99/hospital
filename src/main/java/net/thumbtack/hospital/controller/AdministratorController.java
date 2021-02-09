package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtoresponse.admin.AdminRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditAdminProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditDoctorScheduleDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.service.AdministratorService;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.error.ScheduleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("AdministratorController")
@RequestMapping(AdministratorController.PREFIX_URL)
public class AdministratorController {
    public static final String PREFIX_URL = "/api";
    public static final String ADMINISTRATOR_REGISTRATION_URL = "admins";
    public static final String DOCTOR_REGISTRATION_URL = "doctors";
    public static final String EDIT_ADMINISTRATOR_PROFILE_URL = "admins";
    public static final String EDIT_DOCTOR_SCHEDULE_URL = "doctors/{doctorId}";
    public static final String REMOVE_DOCTOR_URL = "doctors/{doctorId}";

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping(value = ADMINISTRATOR_REGISTRATION_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AdminRegistrationDtoResponse administratorRegistration(@CookieValue(CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                                  @Valid @RequestBody AdminRegistrationDtoRequest request) throws PermissionDeniedException {
        return administratorService.administratorRegistration(sessionId, request);
    }

    @PostMapping(value = DOCTOR_REGISTRATION_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DoctorRegistrationDtoResponse doctorRegistration(@CookieValue(CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                            @Valid @RequestBody DoctorRegistrationDtoRequest request) throws PermissionDeniedException {
        return administratorService.doctorRegistration(sessionId, request);
    }

    @PutMapping(value = EDIT_ADMINISTRATOR_PROFILE_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EditAdminProfileDtoResponse editAdministratorProfile(@CookieValue(CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                                @Valid @RequestBody EditAdminProfileDtoRequest request) throws PermissionDeniedException {
        return administratorService.editAdministratorProfile(sessionId, request);
    }

    @PutMapping(value = EDIT_DOCTOR_SCHEDULE_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EditDoctorScheduleDtoResponse editDoctorSchedule(@CookieValue(CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                            @PathVariable int doctorId,
                                                            @Valid @RequestBody EditDoctorScheduleDtoRequest request)
            throws PermissionDeniedException, ScheduleException {
        return administratorService.editDoctorSchedule(sessionId, doctorId, request);
    }

    @DeleteMapping(value = REMOVE_DOCTOR_URL,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EmptyDtoResponse removeDoctor(@CookieValue(CookieFactory.JAVA_SESSION_ID) String sessionId,
                                         @PathVariable int doctorId,
                                         @Valid @RequestBody RemoveDoctorDtoRequest request) throws PermissionDeniedException {
        return administratorService.removeDoctor(sessionId, doctorId, request);
    }
}