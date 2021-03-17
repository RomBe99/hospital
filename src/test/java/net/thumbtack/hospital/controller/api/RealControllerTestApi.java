package net.thumbtack.hospital.controller.api;

import net.thumbtack.hospital.controller.AdministratorController;
import net.thumbtack.hospital.controller.DoctorController;
import net.thumbtack.hospital.controller.PatientController;
import net.thumbtack.hospital.controller.UserController;
import net.thumbtack.hospital.debug.DebugController;
import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.AdminRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditAdminProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditDoctorScheduleDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.SettingsDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.AppointmentToDoctorDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.EditPatientProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.ticket.AllTicketsDtoResponse;
import net.thumbtack.hospital.dtoresponse.user.GetAllDoctorsDtoResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Objects;

public class RealControllerTestApi extends BaseSpringConfiguration {
    @Autowired
    protected TestRestTemplate restTemplate;

    protected static void putSessionIdToHeaders(HttpHeaders headers, String sessionId) {
        headers.add(HttpHeaders.COOKIE, sessionId);
    }

    @Before
    public void clearDatabase() {
        final String url = buildUrl(DebugController.PREFIX_URL, DebugController.DEBUG_CLEAR_URL);

        final ResponseEntity<EmptyDtoResponse> response = restTemplate.postForEntity(url, HttpEntity.EMPTY, EmptyDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Override
    public <T extends LoginUserDtoResponse> Pair<String, T> login(String login, String password, Class<T> clazz) {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.LOGIN_URL);
        final LoginDtoRequest request = new LoginDtoRequest(login, password);
        final ResponseEntity<T> response = restTemplate.postForEntity(url, request, clazz);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final String sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        Assert.assertNotNull(sessionId);
        Assert.assertFalse(sessionId.isEmpty());

        final T actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());

        return Pair.of(sessionId, actualResponse);
    }

    @Override
    public void logout(String sessionId) {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.LOGOUT_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<EmptyDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), EmptyDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final EmptyDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);

        final EmptyDtoResponse expectedResponse = new EmptyDtoResponse();
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Override
    public <T extends UserInformationDtoResponse> T getUserInformation(String sessionId, Class<T> clazz) {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.GET_USER_INFORMATION_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), clazz);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final T actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());

        return actualResponse;
    }

    @Override
    public DoctorInformationDtoResponse getDoctorInformation(String sessionId, int doctorId, String schedule, String startDate, String endDate) {
        final String url = buildUrlWithPathVariable("doctorId", String.valueOf(doctorId),
                UserController.PREFIX_URL, UserController.GET_DOCTOR_INFORMATION_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("schedule", schedule);


        if (UserController.needSchedule.test(schedule)) {
            requestParams.put("startDate", startDate);
            requestParams.put("endDate", endDate);
        }

        final ResponseEntity<DoctorInformationDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), DoctorInformationDtoResponse.class, requestParams);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final DoctorInformationDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());

        return actualResponse;
    }

    @Override
    public GetAllDoctorsDtoResponse getDoctorsInformation(String sessionId, String schedule, String speciality, String startDate, String endDate) {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.GET_DOCTORS_INFORMATION_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final HashMap<String, String> requestParams = new HashMap<>();
        requestParams.put("schedule", schedule);

        if (speciality != null) {
            requestParams.put("speciality", speciality);
        }

        if (UserController.needSchedule.test(schedule)) {
            requestParams.put("startDate", startDate);
            requestParams.put("endDate", endDate);
        }

        final ResponseEntity<GetAllDoctorsDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), GetAllDoctorsDtoResponse.class, requestParams);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final GetAllDoctorsDtoResponse actualResponse = response.getBody();
        Objects.requireNonNull(actualResponse).getDoctors()
                .forEach(r -> Assert.assertNotEquals(0, r.getId()));

        return actualResponse;
    }

    @Override
    public PatientInformationDtoResponse getPatientInformation(String sessionId, int patientId) {
        final String url = buildUrlWithPathVariable("patientId", String.valueOf(patientId),
                UserController.PREFIX_URL, UserController.GET_PATIENT_INFORMATION_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<PatientInformationDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), PatientInformationDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final PatientInformationDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());

        return actualResponse;
    }

    @Override
    public <T extends SettingsDtoResponse> T getSettings(String sessionId, Class<T> clazz) {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.GET_SETTINGS_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), clazz);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final T actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);

        return actualResponse;
    }

    @Override
    public AdminRegistrationDtoResponse administratorRegistration(String sessionId, AdminRegistrationDtoRequest request) {
        final String url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.ADMINISTRATOR_REGISTRATION_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<AdminRegistrationDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), AdminRegistrationDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final AdminRegistrationDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());

        return actualResponse;
    }

    @Override
    public DoctorRegistrationDtoResponse doctorRegistration(String sessionId, DoctorRegistrationDtoRequest request) {
        final String url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.DOCTOR_REGISTRATION_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<DoctorRegistrationDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), DoctorRegistrationDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final DoctorRegistrationDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());

        return actualResponse;
    }

    @Override
    public EditAdminProfileDtoResponse editAdministratorProfile(String sessionId, EditAdminProfileDtoRequest request) {
        final String url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.EDIT_ADMINISTRATOR_PROFILE_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<EditAdminProfileDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers), EditAdminProfileDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final EditAdminProfileDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());

        return actualResponse;
    }

    @Override
    public EditDoctorScheduleDtoResponse editDoctorSchedule(String sessionId, int doctorId, EditDoctorScheduleDtoRequest request) {
        final String url = buildUrlWithPathVariable("doctorId", String.valueOf(doctorId),
                AdministratorController.PREFIX_URL, AdministratorController.EDIT_DOCTOR_SCHEDULE_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<EditDoctorScheduleDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers), EditDoctorScheduleDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final EditDoctorScheduleDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());

        return actualResponse;
    }

    @Override
    public EmptyDtoResponse removeDoctor(String sessionId, int doctorId, RemoveDoctorDtoRequest request) {
        final String url = buildUrlWithPathVariable("doctorId", String.valueOf(doctorId),
                AdministratorController.PREFIX_URL, AdministratorController.REMOVE_DOCTOR_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<EmptyDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(request, headers), EmptyDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final EmptyDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);

        return actualResponse;
    }

    @Override
    public CreateMedicalCommissionDtoResponse createMedicalCommission(String sessionId, CreateMedicalCommissionDtoRequest request) {
        final String url = buildUrl(DoctorController.PREFIX_URL, DoctorController.CREATE_MEDICAL_COMMISSION_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<CreateMedicalCommissionDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), CreateMedicalCommissionDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final CreateMedicalCommissionDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);

        return actualResponse;
    }

    @Override
    public Pair<String, PatientRegistrationDtoResponse> patientRegistration(PatientRegistrationDtoRequest request) {
        final String url = buildUrl(PatientController.PREFIX_URL, PatientController.PATIENT_REGISTRATION_URL);
        final ResponseEntity<PatientRegistrationDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request), PatientRegistrationDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final String sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        Assert.assertNotNull(sessionId);
        Assert.assertFalse(sessionId.isEmpty());

        final PatientRegistrationDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());
        request.setPhone(actualResponse.getPhone());

        return Pair.of(sessionId, actualResponse);
    }

    @Override
    public EditPatientProfileDtoResponse editPatientProfile(String sessionId, EditPatientProfileDtoRequest request) {
        final String url = buildUrl(PatientController.PREFIX_URL, PatientController.EDIT_PATIENT_PROFILE_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<EditPatientProfileDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(request, headers), EditPatientProfileDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final EditPatientProfileDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);

        return actualResponse;
    }

    @Override
    public AppointmentToDoctorDtoResponse appointmentToDoctor(String sessionId, AppointmentToDoctorDtoRequest request) {
        final String url = buildUrl(PatientController.PREFIX_URL, PatientController.APPOINTMENT_TO_DOCTOR_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<AppointmentToDoctorDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity<>(request, headers), AppointmentToDoctorDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final AppointmentToDoctorDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getDoctorId());

        return actualResponse;
    }

    @Override
    public EmptyDtoResponse denyTicketToDoctor(String sessionId, String ticketTitle) {
        final String url = buildUrlWithPathVariable("ticketTitle", ticketTitle,
                PatientController.PREFIX_URL, PatientController.DENY_TICKET_TO_DOCTOR_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<EmptyDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), EmptyDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final EmptyDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);

        return actualResponse;
    }

    @Override
    public AllTicketsDtoResponse getTickets(String sessionId) {
        final String url = buildUrl(PatientController.PREFIX_URL, PatientController.GET_TICKETS_URL);
        final HttpHeaders headers = new HttpHeaders();
        putSessionIdToHeaders(headers, sessionId);

        final ResponseEntity<AllTicketsDtoResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), AllTicketsDtoResponse.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        final AllTicketsDtoResponse actualResponse = response.getBody();
        Assert.assertNotNull(actualResponse);

        return actualResponse;
    }
}