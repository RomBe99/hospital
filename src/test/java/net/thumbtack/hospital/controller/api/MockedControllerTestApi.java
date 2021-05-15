package net.thumbtack.hospital.controller.api;

import net.thumbtack.hospital.controller.AdministratorController;
import net.thumbtack.hospital.controller.DoctorController;
import net.thumbtack.hospital.controller.PatientController;
import net.thumbtack.hospital.controller.UserController;
import net.thumbtack.hospital.debug.DebugController;
import net.thumbtack.hospital.debug.dtoresponse.schedule.GetScheduleByDoctorIdDtoResponse;
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
import net.thumbtack.hospital.dtoresponse.schedule.DtoResponseWithSchedule;
import net.thumbtack.hospital.dtoresponse.user.GetAllDoctorsDtoResponse;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@AutoConfigureMockMvc
public class MockedControllerTestApi extends BaseSpringConfiguration {
    @Autowired
    protected MockMvc mvc;

    @BeforeEach
    public void clearDatabase() throws Exception {
        final var url = buildUrl(DebugController.PREFIX_URL, DebugController.DEBUG_CLEAR_URL);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var expectedJsonResponse = mapToJson(new EmptyDtoResponse());
        Assertions.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    // Debug controller methods

    private void getScheduleByDoctorId(int doctorId, DtoResponseWithSchedule expectedResponse) throws Exception {
        final var pathVarName = "doctorId";
        final var url = buildUrlWithPathVariable(pathVarName, String.valueOf(doctorId),
                DebugController.PREFIX_URL, DebugController.GET_SCHEDULE_BY_DOCTOR_ID_URL);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var actualResponse = mapFromJson(actualJsonResponse, GetScheduleByDoctorIdDtoResponse.class);
        Assertions.assertEquals(expectedResponse.getSchedule(), actualResponse.getSchedule());
    }

    // User controller methods

    @Override
    public <T extends LoginUserDtoResponse> Pair<String, T> login(String login, String password, Class<T> clazz) throws Exception {
        final var url = buildUrl(UserController.PREFIX_URL, UserController.LOGIN_URL);
        final var request = new LoginDtoRequest(login, password);
        final var json = mapToJson(request);

        final var response = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists(CookieFactory.JAVA_SESSION_ID))
                .andReturn().getResponse();

        final var sessionId = Objects.requireNonNull(response.getCookie(CookieFactory.JAVA_SESSION_ID)).getValue();
        Assertions.assertFalse(sessionId.isEmpty());

        final var actualJsonResponse = response.getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var actualResponse = mapFromJson(actualJsonResponse, clazz);
        Assertions.assertNotEquals(0, actualResponse.getId());

        return Pair.of(sessionId, actualResponse);
    }

    @Override
    public void logout(String sessionId) throws Exception {
        final var url = buildUrl(UserController.PREFIX_URL, UserController.LOGOUT_URL);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .delete(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var expectedJsonResponse = mapToJson(new EmptyDtoResponse());
        Assertions.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    @Override
    public <T extends UserInformationDtoResponse> T getUserInformation(String sessionId, Class<T> clazz) throws Exception {
        final var url = buildUrl(UserController.PREFIX_URL, UserController.GET_USER_INFORMATION_URL);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var actualResponse = mapFromJson(actualJsonResponse, clazz);
        Assertions.assertNotEquals(0, actualResponse.getId());

        return actualResponse;
    }

    @Override
    public DoctorInformationDtoResponse getDoctorInformation(String sessionId, int doctorId, String schedule, String startDate, String endDate) throws Exception {
        final var pathVarName = "doctorId";
        final var url = buildUrlWithPathVariable(pathVarName, String.valueOf(doctorId),
                UserController.PREFIX_URL, UserController.GET_DOCTOR_INFORMATION_URL);

        final var queryParamName = "schedule";
        final var requestBuilder = MockMvcRequestBuilders
                .get(url)
                .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                .characterEncoding(StandardCharsets.UTF_8.name())
                .queryParam(queryParamName, schedule);

        if (UserController.needSchedule.test(schedule)) {
            final var startDateParamName = "startDate";
            requestBuilder.queryParam(startDateParamName, startDate);

            final var endDateParamName = "endDate";
            requestBuilder.queryParam(endDateParamName, endDate);
        }

        final var actualJsonResponse = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, DoctorInformationDtoResponse.class);
    }

    @Override
    public GetAllDoctorsDtoResponse getDoctorsInformation(String sessionId, String schedule, String speciality, String startDate, String endDate) throws Exception {
        final var url = buildUrl(UserController.PREFIX_URL, UserController.GET_DOCTORS_INFORMATION_URL);

        final var queryParamName = "schedule";
        final var requestBuilder = MockMvcRequestBuilders
                .get(url)
                .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                .characterEncoding(StandardCharsets.UTF_8.name())
                .queryParam(queryParamName, schedule);

        if (speciality != null) {
            final var specialityParamName = "speciality";
            requestBuilder.queryParam(specialityParamName, speciality);
        }

        if (UserController.needSchedule.test(schedule)) {
            final var startDateParamName = "startDate";
            requestBuilder.queryParam(startDateParamName, startDate);

            final var endDateParamName = "endDate";
            requestBuilder.queryParam(endDateParamName, endDate);
        }

        final var actualJsonResponse = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, GetAllDoctorsDtoResponse.class);
    }

    @Override
    public PatientInformationDtoResponse getPatientInformation(String sessionId, int patientId) throws Exception {
        final var pathVarName = "patientId";
        final var url = buildUrlWithPathVariable(pathVarName, String.valueOf(patientId),
                UserController.PREFIX_URL, UserController.GET_PATIENT_INFORMATION_URL);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, PatientInformationDtoResponse.class);
    }

    @Override
    public <T extends SettingsDtoResponse> T getSettings(String sessionId, Class<T> clazz) throws Exception {
        final var url = buildUrl(UserController.PREFIX_URL, UserController.GET_SETTINGS_URL);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, clazz);
    }

    // Admin controller methods

    @Override
    public AdminRegistrationDtoResponse administratorRegistration(String sessionId, AdminRegistrationDtoRequest request) throws Exception {
        final var url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.ADMINISTRATOR_REGISTRATION_URL);
        final var json = mapToJson(request);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var actualResponse = mapFromJson(actualJsonResponse, AdminRegistrationDtoResponse.class);
        Assertions.assertNotEquals(0, actualResponse.getId());

        return actualResponse;
    }

    public DoctorRegistrationDtoResponse doctorRegistration(String sessionId, DoctorRegistrationDtoRequest request) throws Exception {
        final var url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.DOCTOR_REGISTRATION_URL);
        final var json = mapToJson(request);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var actualResponse = mapFromJson(actualJsonResponse, DoctorRegistrationDtoResponse.class);
        Assertions.assertNotEquals(0, actualResponse.getId());
        actualResponse.getSchedule().forEach(Assertions::assertNotNull);

        return actualResponse;
    }

    @Override
    public EditAdminProfileDtoResponse editAdministratorProfile(String sessionId, EditAdminProfileDtoRequest request) throws Exception {
        final var url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.EDIT_ADMINISTRATOR_PROFILE_URL);
        final var json = mapToJson(request);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, EditAdminProfileDtoResponse.class);
    }

    @Override
    public EditDoctorScheduleDtoResponse editDoctorSchedule(String sessionId, int doctorId, EditDoctorScheduleDtoRequest request) throws Exception {
        final var pathVarName = "doctorId";
        final var url = buildUrlWithPathVariable(pathVarName, String.valueOf(doctorId),
                AdministratorController.PREFIX_URL, AdministratorController.EDIT_DOCTOR_SCHEDULE_URL);
        final var json = mapToJson(request);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, EditDoctorScheduleDtoResponse.class);
    }

    @Override
    public EmptyDtoResponse removeDoctor(String sessionId, int doctorId, RemoveDoctorDtoRequest request) throws Exception {
        final var pathVarName = "doctorId";
        final var url = buildUrlWithPathVariable(pathVarName, String.valueOf(doctorId),
                AdministratorController.PREFIX_URL, AdministratorController.REMOVE_DOCTOR_URL);
        final var json = mapToJson(request);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .delete(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, EmptyDtoResponse.class);
    }

    // Patient controller methods

    public Pair<String, PatientRegistrationDtoResponse> patientRegistration(PatientRegistrationDtoRequest request) throws Exception {
        final var url = buildUrl(PatientController.PREFIX_URL, PatientController.PATIENT_REGISTRATION_URL);
        final var json = mapToJson(request);

        final var response = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists(CookieFactory.JAVA_SESSION_ID))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        final var sessionId = Objects.requireNonNull(response.getCookie(CookieFactory.JAVA_SESSION_ID)).getValue();
        Assertions.assertFalse(sessionId.isEmpty());

        final var actualJsonResponse = response.getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var actualResponse = mapFromJson(actualJsonResponse, PatientRegistrationDtoResponse.class);
        Assertions.assertNotEquals(0, actualResponse.getId());

        request.setPhone(actualResponse.getPhone());

        return Pair.of(sessionId, actualResponse);
    }

    @Override
    public EditPatientProfileDtoResponse editPatientProfile(String sessionId, EditPatientProfileDtoRequest request) throws Exception {
        final var url = buildUrl(PatientController.PREFIX_URL, PatientController.EDIT_PATIENT_PROFILE_URL);
        final var json = mapToJson(request);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var actualResponse = mapFromJson(actualJsonResponse, EditPatientProfileDtoResponse.class);
        request.setPhone(actualResponse.getPhone());

        return actualResponse;
    }

    @Override
    public AppointmentToDoctorDtoResponse appointmentToDoctor(String sessionId, AppointmentToDoctorDtoRequest request) throws Exception {
        final var url = buildUrl(PatientController.PREFIX_URL, PatientController.APPOINTMENT_TO_DOCTOR_URL);
        final var json = mapToJson(request);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, AppointmentToDoctorDtoResponse.class);
    }

    @Override
    public EmptyDtoResponse denyTicketToDoctor(String sessionId, String ticketTitle) throws Exception {
        final var pathVarName = "ticketTitle";
        final var url = buildUrlWithPathVariable(pathVarName, ticketTitle, PatientController.PREFIX_URL, PatientController.DENY_TICKET_TO_DOCTOR_URL);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .delete(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, EmptyDtoResponse.class);
    }

    @Override
    public AllTicketsDtoResponse getTickets(String sessionId) throws Exception {
        final var url = buildUrl(PatientController.PREFIX_URL, PatientController.GET_TICKETS_URL);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, AllTicketsDtoResponse.class);
    }

    // Doctor controller methods

    @Override
    public CreateMedicalCommissionDtoResponse createMedicalCommission(String sessionId, CreateMedicalCommissionDtoRequest request) throws Exception {
        final var url = buildUrl(DoctorController.PREFIX_URL, DoctorController.CREATE_MEDICAL_COMMISSION_URL);
        final var json = mapToJson(request);

        final var actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, CreateMedicalCommissionDtoResponse.class);
    }
}