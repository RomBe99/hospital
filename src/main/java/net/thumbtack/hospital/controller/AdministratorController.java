package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtoresponse.admin.AdminRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditAdminProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditDoctorScheduleDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.service.AdministratorService;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("AdministratorController")
@RequestMapping("/api")
public class AdministratorController {
    public static final String administratorRegistrationUrl = "admins";
    public static final String doctorRegistrationUrl = "doctors";
    public static final String editAdministratorProfileUrl = "admins";
    public static final String editDoctorScheduleUrl = "doctors/{doctorId}";
    public static final String removeDoctorUrl = "doctors/{doctorId}";

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping(value = administratorRegistrationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AdminRegistrationDtoResponse administratorRegistration(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                                  @Valid @RequestBody AdminRegistrationDtoRequest request) {
        return administratorService.administratorRegistration(sessionId, request);
    }

    @PostMapping(value = doctorRegistrationUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DoctorRegistrationDtoResponse doctorRegistration(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                            @Valid @RequestBody DoctorRegistrationDtoRequest request) {
        return administratorService.doctorRegistration(sessionId, request);
    }

    @PutMapping(value = editAdministratorProfileUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EditAdminProfileDtoResponse editAdministratorProfile(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                                @Valid @RequestBody EditAdminProfileDtoRequest request) {
        return administratorService.editAdministratorProfile(sessionId, request);
    }

    @PutMapping(value = editDoctorScheduleUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EditDoctorScheduleDtoResponse editDoctorSchedule(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                                            @PathVariable int doctorId,
                                                            @Valid @RequestBody EditDoctorScheduleDtoRequest request) {
        return administratorService.editDoctorSchedule(sessionId, doctorId, request);
    }

    @DeleteMapping(value = removeDoctorUrl,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EmptyDtoResponse removeDoctor(@CookieValue(value = CookieFactory.JAVA_SESSION_ID) String sessionId,
                                         @PathVariable int doctorId,
                                         @Valid @RequestBody RemoveDoctorDtoRequest request) {
        return administratorService.removeDoctor(sessionId, doctorId, request);
    }
}