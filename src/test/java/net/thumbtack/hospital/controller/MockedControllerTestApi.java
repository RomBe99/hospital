package net.thumbtack.hospital.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.hospital.debug.DebugController;
import net.thumbtack.hospital.debug.dtoresponse.schedule.GetScheduleByDoctorIdDtoResponse;
import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.*;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.SettingsDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.*;
import net.thumbtack.hospital.dtoresponse.patient.ticket.AllTicketsDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.DtoResponseWithSchedule;
import net.thumbtack.hospital.dtoresponse.user.GetAllDoctorsDtoResponse;
import net.thumbtack.hospital.server.HospitalApplication;
import net.thumbtack.hospital.util.adapter.DtoAdapters;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@SpringBootTest(classes = HospitalApplication.class)
@AutoConfigureMockMvc
public abstract class MockedControllerTestApi {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    private ObjectMapper jsonMapper;

    @BeforeClass
    public static void setUpDatabase() {
        MyBatisUtils.initConnection();
    }

    @Before
    public void clearDatabase() throws Exception {
        final String url = buildUrl(DebugController.PREFIX_URL, DebugController.DEBUG_CLEAR_URL);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final String expectedJsonResponse = mapToJson(new EmptyDtoResponse());
        Assert.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    public static String buildUrl(String... urlParts) {
        final String separator = "/";

        if (urlParts.length == 0) {
            return separator;
        }

        StringJoiner sj = new StringJoiner(separator);

        for (String part : urlParts) {
            sj.add(part);
        }

        final String result = sj.toString();

        return result.startsWith(separator) ? result : separator + result;
    }

    public static String buildUrlWithPathVariable(String pathVarName, String pathVarValue, String... urlParts) {
        final String nameWithBrackets = '{' + pathVarName + '}';

        return buildUrl(urlParts).replace(nameWithBrackets, pathVarValue);
    }

    public String mapToJson(Object obj) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(obj);
    }

    public <T> T mapFromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return jsonMapper.readValue(json, clazz);
    }

    // Debug controller methods

    private void getScheduleByDoctorId(int doctorId, DtoResponseWithSchedule expectedResponse) throws Exception {
        final String pathVarName = "doctorId";
        final String url = buildUrlWithPathVariable(pathVarName, String.valueOf(doctorId),
                DebugController.PREFIX_URL, DebugController.GET_SCHEDULE_BY_DOCTOR_ID_URL);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final GetScheduleByDoctorIdDtoResponse actualResponse = mapFromJson(actualJsonResponse, GetScheduleByDoctorIdDtoResponse.class);
        Assert.assertEquals(expectedResponse.getSchedule(), actualResponse.getSchedule());
    }

    // User controller methods

    public String login(String login, String password, LoginUserDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.LOGIN_URL);
        final LoginDtoRequest request = new LoginDtoRequest(login, password);
        final String json = mapToJson(request);

        final MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists(CookieFactory.JAVA_SESSION_ID))
                .andReturn().getResponse();

        final String userSessionId = Objects.requireNonNull(response.getCookie(CookieFactory.JAVA_SESSION_ID)).getValue();
        Assert.assertFalse(userSessionId.isEmpty());

        final String actualJsonResponse = response.getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final LoginUserDtoResponse actualResponse = mapFromJson(actualJsonResponse, expectedResponse.getClass());
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());

        expectedResponse.setId(actualResponse.getId());
        Assert.assertEquals(expectedResponse, actualResponse);

        return userSessionId;
    }

    public String loginRootAdmin() throws Exception {
        final String login = "admin";
        final String password = "admin";
        final LoginUserDtoResponse expectedResponse =
                new AdminLoginDtoResponse("Roman", "Belinsky", null, "Root admin");

        return login(login, password, expectedResponse);
    }

    public void logout(String sessionId) throws Exception {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.LOGOUT_URL);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .delete(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final String expectedJsonResponse = mapToJson(new EmptyDtoResponse());
        Assert.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    public void getUserInformation(String sessionId, UserInformationDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.GET_USER_INFORMATION_URL);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final UserInformationDtoResponse actualResponse = mapFromJson(actualJsonResponse, expectedResponse.getClass());
        Assert.assertNotEquals(0, actualResponse.getId());
        expectedResponse.setId(actualResponse.getId());

        if (FullPatientInformationDtoResponse.class == expectedResponse.getClass()) {
            ((FullPatientInformationDtoResponse) expectedResponse)
                    .setPhone(((FullPatientInformationDtoResponse) actualResponse).getPhone());
        }

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void getDoctorInformation(String sessionId, int doctorId, String schedule, String startDate, String endDate,
                                     DoctorInformationDtoResponse expectedResponse) throws Exception {
        final String pathVarName = "doctorId";
        final String url = buildUrlWithPathVariable(pathVarName, String.valueOf(doctorId),
                UserController.PREFIX_URL, UserController.GET_DOCTOR_INFORMATION_URL);

        final String queryParamName = "schedule";
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                .characterEncoding(StandardCharsets.UTF_8.name())
                .queryParam(queryParamName, schedule);

        if (startDate != null && endDate != null) {
            final String startDateParamName = "startDate";
            requestBuilder.queryParam(startDateParamName, startDate);

            final String endDateParamName = "endDate";
            requestBuilder.queryParam(endDateParamName, endDate);
        }

        final String actualJsonResponse = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final DoctorInformationDtoResponse actualResponse = mapFromJson(actualJsonResponse, DoctorInformationDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void getDoctorsInformation(String sessionId, String schedule, String speciality, String startDate, String endDate,
                                      GetAllDoctorsDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.GET_DOCTORS_INFORMATION_URL);

        final String queryParamName = "schedule";
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                .characterEncoding(StandardCharsets.UTF_8.name())
                .queryParam(queryParamName, schedule);

        if (speciality != null) {
            final String specialityParamName = "speciality";
            requestBuilder.queryParam(specialityParamName, speciality);
        }

        if (startDate != null && endDate != null) {
            final String startDateParamName = "startDate";
            requestBuilder.queryParam(startDateParamName, startDate);

            final String endDateParamName = "endDate";
            requestBuilder.queryParam(endDateParamName, endDate);
        }

        final String actualJsonResponse = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final GetAllDoctorsDtoResponse actualResponse = mapFromJson(actualJsonResponse, GetAllDoctorsDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void getPatientInformation(String sessionId, int patientId,
                                      PatientInformationDtoResponse expectedResponse) throws Exception {
        final String pathVarName = "patientId";
        final String url = buildUrlWithPathVariable(pathVarName, String.valueOf(patientId),
                UserController.PREFIX_URL, UserController.GET_PATIENT_INFORMATION_URL);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assert.assertFalse(actualJsonResponse.isEmpty());

        final PatientInformationDtoResponse actualResponse = mapFromJson(actualJsonResponse, PatientInformationDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void getSettings(String sessionId, SettingsDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.GET_SETTINGS_URL);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final SettingsDtoResponse actualResponse = mapFromJson(actualJsonResponse, expectedResponse.getClass());
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    // Admin controller methods

    public void administratorRegistration(String sessionId, AdminRegistrationDtoRequest request,
                                          AdminRegistrationDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.ADMINISTRATOR_REGISTRATION_URL);
        final String json = mapToJson(request);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final AdminRegistrationDtoResponse actualResponse = mapFromJson(actualJsonResponse, AdminRegistrationDtoResponse.class);
        Assert.assertNotEquals(0, actualResponse.getId());
        expectedResponse.setId(actualResponse.getId());
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void doctorRegistration(String sessionId, DoctorRegistrationDtoRequest request,
                                   DoctorRegistrationDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.DOCTOR_REGISTRATION_URL);
        final String json = mapToJson(request);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final DoctorRegistrationDtoResponse actualResponse = mapFromJson(actualJsonResponse, DoctorRegistrationDtoResponse.class);
        Assert.assertNotEquals(0, actualResponse.getId());
        expectedResponse.setId(actualResponse.getId());
        expectedResponse.setSchedule(DtoAdapters.transform(request, actualResponse.getId()).stream()
                .map(DtoAdapters::transform)
                .collect(Collectors.toList()));
        Assert.assertEquals(expectedResponse, actualResponse);

        getScheduleByDoctorId(expectedResponse.getId(), expectedResponse);
    }

    public void editAdministratorProfile(String sessionId, EditAdminProfileDtoRequest request,
                                         EditAdminProfileDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.EDIT_ADMINISTRATOR_PROFILE_URL);
        final String json = mapToJson(request);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final EditAdminProfileDtoResponse actualResponse = mapFromJson(actualJsonResponse, EditAdminProfileDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void editDoctorSchedule(String sessionId, int doctorId, EditDoctorScheduleDtoRequest request,
                                   EditDoctorScheduleDtoResponse expectedResponse) throws Exception {
        final String pathVarName = "doctorId";
        final String url = buildUrlWithPathVariable(pathVarName, String.valueOf(doctorId),
                AdministratorController.PREFIX_URL, AdministratorController.EDIT_DOCTOR_SCHEDULE_URL);
        final String json = mapToJson(request);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final EditDoctorScheduleDtoResponse actualResponse = mapFromJson(actualJsonResponse, EditDoctorScheduleDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void removeDoctor(String sessionId, int doctorId, RemoveDoctorDtoRequest request) throws Exception {
        final String pathVarName = "doctorId";
        final String url = buildUrlWithPathVariable(pathVarName, String.valueOf(doctorId),
                AdministratorController.PREFIX_URL, AdministratorController.REMOVE_DOCTOR_URL);
        final String json = mapToJson(request);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .delete(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final String expectedJsonResponse = mapToJson(new EmptyDtoResponse());
        Assert.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    // Patient controller methods

    public String patientRegistration(PatientRegistrationDtoRequest request,
                                      PatientRegistrationDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(PatientController.PREFIX_URL, PatientController.PATIENT_REGISTRATION_URL);
        final String json = mapToJson(request);

        final MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists(CookieFactory.JAVA_SESSION_ID))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        final String patientSessionId = Objects.requireNonNull(response.getCookie(CookieFactory.JAVA_SESSION_ID)).getValue();
        Assert.assertFalse(patientSessionId.isEmpty());

        final String actualJsonResponse = response.getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final PatientRegistrationDtoResponse actualResponse = mapFromJson(actualJsonResponse, PatientRegistrationDtoResponse.class);
        Assert.assertNotEquals(0, actualResponse.getId());
        expectedResponse.setId(actualResponse.getId());
        expectedResponse.setPhone(actualResponse.getPhone());
        request.setPhone(actualResponse.getPhone());
        Assert.assertEquals(expectedResponse, actualResponse);

        return patientSessionId;
    }

    public void editPatientProfile(String sessionId, EditPatientProfileDtoRequest request,
                                   EditPatientProfileDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(PatientController.PREFIX_URL, PatientController.EDIT_PATIENT_PROFILE_URL);
        final String json = mapToJson(request);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final EditPatientProfileDtoResponse actualResponse = mapFromJson(actualJsonResponse, EditPatientProfileDtoResponse.class);
        request.setPhone(actualResponse.getPhone());
        expectedResponse.setPhone(actualResponse.getPhone());
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public AppointmentToDoctorDtoResponse appointmentToDoctor(String sessionId, AppointmentToDoctorDtoRequest request) throws Exception {
        final String url = buildUrl(PatientController.PREFIX_URL, PatientController.APPOINTMENT_TO_DOCTOR_URL);
        final String json = mapToJson(request);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        return mapFromJson(actualJsonResponse, AppointmentToDoctorDtoResponse.class);
    }

    public void denyTicketToDoctor(String sessionId, String ticketTitle) throws Exception {
        final String pathVarName = "ticketTitle";
        final String url = buildUrlWithPathVariable(pathVarName, ticketTitle, PatientController.PREFIX_URL, PatientController.DENY_TICKET_TO_DOCTOR_URL);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .delete(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final String expectedJsonResponse = mapToJson(new EmptyDtoResponse());
        Assert.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    public void getTickets(String sessionId, AllTicketsDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(PatientController.PREFIX_URL, PatientController.GET_TICKETS_URL);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final AllTicketsDtoResponse actualResponse = mapFromJson(actualJsonResponse, AllTicketsDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    // Doctor controller methods

    public void createMedicalCommission(String sessionId, CreateMedicalCommissionDtoRequest request,
                                        CreateMedicalCommissionDtoResponse expectedResponse) throws Exception {
        final String url = buildUrl(DoctorController.PREFIX_URL, DoctorController.CREATE_MEDICAL_COMMISSION_URL);
        final String json = mapToJson(request);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final CreateMedicalCommissionDtoResponse actualResponse = mapFromJson(actualJsonResponse, CreateMedicalCommissionDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }
}