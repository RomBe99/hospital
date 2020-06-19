package net.thumbtack.hospital.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.hospital.debug.DebugController;
import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.AdminInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.FullPatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
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
                        .post(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();

        String expectedJsonResponse = "{}";

        Assert.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }

    protected String login(String login, String password) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.LOGIN_URL;
        LoginDtoRequest request = new LoginDtoRequest(login, password);
        String json = mapToJson(request);

        String sessionId = Objects.requireNonNull(mvc
                .perform(MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists(CookieFactory.JAVA_SESSION_ID))
                .andReturn().getResponse().getCookie(CookieFactory.JAVA_SESSION_ID)).getValue();

        Assert.assertFalse(sessionId.isEmpty());

        return sessionId;
    }

    protected void logout(String sessionId) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.LOGOUT_URL;

        String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .delete(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();

        String expectedJsonResponse = "{}";

        Assert.assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    protected UserInformationDtoResponse getUserInformation(String sessionId, UserTypes userType) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.GET_USER_INFORMATION_URL;

        String jsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();

        Assert.assertFalse(jsonResponse.isEmpty());

        if (UserTypes.PATIENT.equals(userType)) {
            return mapFromJson(jsonResponse, FullPatientInformationDtoResponse.class);
        }

        if (UserTypes.ADMINISTRATOR.equals(userType)) {
            return mapFromJson(jsonResponse, AdminInformationDtoResponse.class);
        }

        if (UserTypes.DOCTOR.equals(userType)) {
            return mapFromJson(jsonResponse, DoctorInformationDtoResponse.class);
        }

        throw new RuntimeException("Test: Undefined user type. Can't get user information");
    }

    protected DoctorInformationDtoResponse getDoctorInformation(String sessionId, int doctorId, String schedule, String startDate, String endDate) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.GET_DOCTOR_INFORMATION_URL.replace("{doctorId}", String.valueOf(doctorId));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                .queryParam("schedule", schedule);

        if (startDate != null) {
            requestBuilder.queryParam("startDate", startDate);
        }

        if (endDate != null) {
            requestBuilder.queryParam("endDate", endDate);
        }

        String jsonResponse = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();

        Assert.assertFalse(jsonResponse.isEmpty());

        return mapFromJson(jsonResponse, DoctorInformationDtoResponse.class);
    }

    protected GetAllDoctorsDtoResponse getDoctorsInformation(String sessionId, String schedule, String speciality, String startDate, String endDate) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.GET_DOCTORS_INFORMATION_URL;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId))
                .queryParam("schedule", schedule);

        if (speciality != null) {
            requestBuilder.queryParam("speciality", speciality);
        }

        if (startDate != null) {
            requestBuilder.queryParam("startDate", startDate);
        }

        if (endDate != null) {
            requestBuilder.queryParam("endDate", endDate);
        }

        String jsonResponse = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();

        Assert.assertFalse(jsonResponse.isEmpty());

        return mapFromJson(jsonResponse, GetAllDoctorsDtoResponse.class);
    }

    protected PatientInformationDtoResponse getPatientInformation(String sessionId, int patientId) throws Exception {
        String url = UserController.PREFIX_URL + "/" + UserController.GET_PATIENT_INFORMATION_URL.replace("{patientId}", String.valueOf(patientId));

        String jsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, sessionId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();

        Assert.assertFalse(jsonResponse.isEmpty());

        return mapFromJson(jsonResponse, PatientInformationDtoResponse.class);
    }
}