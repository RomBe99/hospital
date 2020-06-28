package net.thumbtack.hospital.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.hospital.debug.DebugController;
import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.*;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.*;
import net.thumbtack.hospital.dtoresponse.patient.ticket.AllTicketsDtoResponse;
import net.thumbtack.hospital.dtoresponse.user.GetAllDoctorsDtoResponse;
import net.thumbtack.hospital.mapper.UserTypes;
import net.thumbtack.hospital.server.HospitalApplication;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootTest(classes = HospitalApplication.class)
@AutoConfigureMockMvc
public abstract class BaseControllerTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static boolean setUpDatabaseIsDone = false;

    @BeforeClass
    public static void setUpDatabase() {
        if (!setUpDatabaseIsDone) {
            boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();

            if (!initSqlSessionFactory) {
                throw new RuntimeException("Can't create connection, stop");
            }

            setUpDatabaseIsDone = true;
        }
    }

    @Before
    public void clearDatabase() throws Exception {
        String url = DebugController.PREFIX_URL + "/" + DebugController.DEBUG_CLEAR_URL;

        String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();

        String expectedJsonResponse = mapToJson(new EmptyDtoResponse());

        Assert.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    public String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public <T> T mapFromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }

    // User controller methods

    public String login(String login, String password, LoginUserDtoResponse expectedResponse, UserTypes userType) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.LOGIN_URL;
        LoginDtoRequest request = new LoginDtoRequest(login, password);
        String json = mapToJson(request);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists(CookieFactory.JAVA_SESSION_ID))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<UserTypes, Class<? extends LoginUserDtoResponse>> dtoClasses = new HashMap<>();
        dtoClasses.put(UserTypes.ADMINISTRATOR, AdminLoginDtoResponse.class);
        dtoClasses.put(UserTypes.DOCTOR, DoctorLoginDtoResponse.class);
        dtoClasses.put(UserTypes.PATIENT, PatientLoginDtoResponse.class);

        LoginUserDtoResponse actualResponse = mapFromJson(response.getContentAsString(), dtoClasses.get(userType));
        Assert.assertNotNull(actualResponse);
        Assert.assertNotEquals(0, actualResponse.getId());

        expectedResponse.setId(actualResponse.getId());
        Assert.assertEquals(expectedResponse, actualResponse);

        String userSessionId = Objects.requireNonNull(response.getCookie(CookieFactory.JAVA_SESSION_ID)).getValue();
        Assert.assertFalse(userSessionId.isEmpty());

        return userSessionId;
    }

    public String loginRootAdmin() throws Exception {
        String login = "admin";
        String password = "admin";
        LoginUserDtoResponse expectedResponse = new AdminLoginDtoResponse("Roman", "Belinsky", "Root admin");

        return login(login, password, expectedResponse, UserTypes.ADMINISTRATOR);
    }

    public void logout(String sessionId) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.LOGOUT_URL;

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .delete(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String actualJsonResponse = response.getContentAsString();
        String expectedJsonResponse = mapToJson(new EmptyDtoResponse());
        Assert.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    public void getUserInformation(String sessionId, UserTypes userType,
                                   UserInformationDtoResponse expectedResponse) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.GET_USER_INFORMATION_URL;

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String jsonResponse = response.getContentAsString();

        Assert.assertFalse(jsonResponse.isEmpty());

        Map<UserTypes, Class<? extends UserInformationDtoResponse>> dtoClasses = new HashMap<>();
        dtoClasses.put(UserTypes.ADMINISTRATOR, AdminInformationDtoResponse.class);
        dtoClasses.put(UserTypes.DOCTOR, DoctorInformationDtoResponse.class);
        dtoClasses.put(UserTypes.PATIENT, FullPatientInformationDtoResponse.class);

        UserInformationDtoResponse actualResponse = mapFromJson(jsonResponse, dtoClasses.get(userType));
        Assert.assertNotEquals(0, actualResponse.getId());
        expectedResponse.setId(actualResponse.getId());

        if (UserTypes.PATIENT.equals(userType)) {
            ((FullPatientInformationDtoResponse) expectedResponse)
                    .setPhone(((FullPatientInformationDtoResponse) actualResponse).getPhone());
        }

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void getDoctorInformation(String sessionId, int doctorId, String schedule, String startDate, String endDate,
                                     DoctorInformationDtoResponse expectedResponse) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.GET_DOCTOR_INFORMATION_URL.replace("{doctorId}", String.valueOf(doctorId));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                .characterEncoding(StandardCharsets.UTF_8.name())
                .queryParam("schedule", schedule);

        if (startDate != null && endDate != null) {
            requestBuilder.queryParam("startDate", startDate);
            requestBuilder.queryParam("endDate", endDate);
        }

        MockHttpServletResponse response = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String jsonResponse = response.getContentAsString();
        Assert.assertFalse(jsonResponse.isEmpty());

        DoctorInformationDtoResponse actualResponse = mapFromJson(jsonResponse, DoctorInformationDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void getDoctorsInformation(String sessionId, String schedule, String speciality, String startDate, String endDate,
                                      GetAllDoctorsDtoResponse expectedResponse) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.GET_DOCTORS_INFORMATION_URL;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                .characterEncoding(StandardCharsets.UTF_8.name())
                .queryParam("schedule", schedule);

        if (speciality != null) {
            requestBuilder.queryParam("speciality", speciality);
        }

        if (startDate != null && endDate != null) {
            requestBuilder.queryParam("startDate", startDate);
            requestBuilder.queryParam("endDate", endDate);
        }

        MockHttpServletResponse response = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String jsonResponse = response.getContentAsString();
        Assert.assertFalse(jsonResponse.isEmpty());

        GetAllDoctorsDtoResponse actualResponse = mapFromJson(jsonResponse, GetAllDoctorsDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void getPatientInformation(String sessionId, int patientId,
                                      PatientInformationDtoResponse expectedResponse) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.GET_PATIENT_INFORMATION_URL.replace("{patientId}", String.valueOf(patientId));

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String jsonResponse = response.getContentAsString();
        Assert.assertFalse(jsonResponse.isEmpty());

        PatientInformationDtoResponse actualResponse = mapFromJson(jsonResponse, PatientInformationDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    // Admin controller methods

    public void administratorRegistration(String sessionId, AdminRegistrationDtoRequest request,
                                          AdminRegistrationDtoResponse expectedResponse) throws Exception {
        String url = AdministratorController.PREFIX_URL + "/" + AdministratorController.ADMINISTRATOR_REGISTRATION_URL;
        String json = mapToJson(request);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String actualJsonResponse = response.getContentAsString();
        Assert.assertFalse(actualJsonResponse.isEmpty());

        AdminRegistrationDtoResponse actualResponse = mapFromJson(actualJsonResponse, AdminRegistrationDtoResponse.class);
        Assert.assertNotEquals(0, actualResponse.getId());
        expectedResponse.setId(actualResponse.getId());
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void doctorRegistration(String sessionId, DoctorRegistrationDtoRequest request,
                                   DoctorRegistrationDtoResponse expectedResponse) throws Exception {
        String url = AdministratorController.PREFIX_URL + "/" + AdministratorController.DOCTOR_REGISTRATION_URL;
        String json = mapToJson(request);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String actualJsonResponse = response.getContentAsString();
        Assert.assertFalse(actualJsonResponse.isEmpty());

        DoctorRegistrationDtoResponse actualResponse = mapFromJson(actualJsonResponse, DoctorRegistrationDtoResponse.class);
        Assert.assertNotEquals(0, actualResponse.getId());
        expectedResponse.setId(actualResponse.getId());
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void editAdministratorProfile(String sessionId, EditAdminProfileDtoRequest request,
                                         EditAdminProfileDtoResponse expectedResponse) throws Exception {
        String url = AdministratorController.PREFIX_URL + "/" + AdministratorController.EDIT_ADMINISTRATOR_PROFILE_URL;
        String json = mapToJson(request);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String actualJsonResponse = response.getContentAsString();
        Assert.assertFalse(actualJsonResponse.isEmpty());

        EditAdminProfileDtoResponse actualResponse = mapFromJson(actualJsonResponse, EditAdminProfileDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void editDoctorSchedule(String sessionId, int doctorId, EditDoctorScheduleDtoRequest request,
                                   EditDoctorScheduleDtoResponse expectedResponse) throws Exception {
        String url = AdministratorController.PREFIX_URL + "/" + AdministratorController.EDIT_DOCTOR_SCHEDULE_URL.replace("{doctorId}", String.valueOf(doctorId));
        String json = mapToJson(request);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String actualJsonResponse = response.getContentAsString();
        Assert.assertFalse(actualJsonResponse.isEmpty());

        EditDoctorScheduleDtoResponse actualResponse = mapFromJson(actualJsonResponse, EditDoctorScheduleDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void removeDoctor(String sessionId, int doctorId, RemoveDoctorDtoRequest request) throws Exception {
        String url = AdministratorController.PREFIX_URL + "/" + AdministratorController.REMOVE_DOCTOR_URL.replace("{doctorId}", String.valueOf(doctorId));
        String json = mapToJson(request);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .delete(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String actualJsonResponse = response.getContentAsString();
        String expectedJsonResponse = mapToJson(new EmptyDtoResponse());
        Assert.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    // Patient controller methods

    public String patientRegistration(PatientRegistrationDtoRequest request,
                                    PatientRegistrationDtoResponse expectedResponse) throws Exception {
        String url = PatientController.PREFIX_URL + "/" + PatientController.PATIENT_REGISTRATION_URL;
        String json = mapToJson(request);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists(CookieFactory.JAVA_SESSION_ID))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String patientSessionId = Objects.requireNonNull(response.getCookie(CookieFactory.JAVA_SESSION_ID)).getValue();
        Assert.assertFalse(patientSessionId.isEmpty());

        String actualJsonResponse = response.getContentAsString();
        Assert.assertFalse(actualJsonResponse.isEmpty());

        PatientRegistrationDtoResponse actualResponse = mapFromJson(actualJsonResponse, PatientRegistrationDtoResponse.class);
        Assert.assertNotEquals(0, actualResponse.getId());
        expectedResponse.setId(actualResponse.getId());
        expectedResponse.setPhone(actualResponse.getPhone());
        request.setPhone(actualResponse.getPhone());
        Assert.assertEquals(expectedResponse, actualResponse);

        return patientSessionId;
    }

    public void editPatientProfile(String sessionId, EditPatientProfileDtoRequest request,
                                   EditPatientProfileDtoResponse expectedResponse) throws Exception {
        String url = PatientController.PREFIX_URL + "/" + PatientController.EDIT_PATIENT_PROFILE_URL;
        String json = mapToJson(request);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String actualJsonResponse = response.getContentAsString();
        Assert.assertFalse(actualJsonResponse.isEmpty());

        EditPatientProfileDtoResponse actualResponse = mapFromJson(actualJsonResponse, EditPatientProfileDtoResponse.class);
        request.setPhone(actualResponse.getPhone());
        expectedResponse.setPhone(actualResponse.getPhone());
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void appointmentToDoctor(String sessionId, AppointmentToDoctorDtoRequest request,
                                    AppointmentToDoctorDtoResponse expectedResponse) throws Exception {
        String url = PatientController.PREFIX_URL + "/" + PatientController.APPOINTMENT_TO_DOCTOR_URL;
        String json = mapToJson(request);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String actualJsonResponse = response.getContentAsString();
        Assert.assertFalse(actualJsonResponse.isEmpty());

        AppointmentToDoctorDtoResponse actualResponse = mapFromJson(actualJsonResponse, AppointmentToDoctorDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    public void getTickets(String sessionId, AllTicketsDtoResponse expectedResponse) throws Exception {
        String url = PatientController.PREFIX_URL + "/" + PatientController.GET_TICKETS_URL;

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String actualJsonResponse = response.getContentAsString();
        Assert.assertFalse(actualJsonResponse.isEmpty());

        AllTicketsDtoResponse actualResponse = mapFromJson(actualJsonResponse, AllTicketsDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    // Doctor controller methods

    public void createMedicalCommission(String sessionId, CreateMedicalCommissionDtoRequest request,
                                        CreateMedicalCommissionDtoResponse expectedResponse) throws Exception {
        String url = DoctorController.PREFIX_URL + "/" + DoctorController.CREATE_MEDICAL_COMMISSION_URL;
        String json = mapToJson(request);

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String actualJsonResponse = response.getContentAsString();
        Assert.assertFalse(actualJsonResponse.isEmpty());

        CreateMedicalCommissionDtoResponse actualResponse = mapFromJson(actualJsonResponse, CreateMedicalCommissionDtoResponse.class);
        Assert.assertEquals(expectedResponse, actualResponse);
    }
}