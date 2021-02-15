package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.other.ErrorDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ErrorsDtoResponse;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.ErrorMessageFactory;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;
import java.util.function.BiFunction;

@RunWith(SpringRunner.class)
public class GlobalMockedControllerExceptionHandlerTest extends MockedControllerTestApi {
    @Autowired
    private ErrorMessageFactory errorMessageFactory;
    private final BiFunction<String, String, ErrorDtoResponse> genErrorResponse = (String errCode, String field) ->
            new ErrorDtoResponse(errCode, field, this.errorMessageFactory.getValidationMessageByCode(errCode));

    @Test
    public void incorrectPostUrlTest() throws Exception {
        String url = "/incorrect_url";

        mvc.perform(MockMvcRequestBuilders.post(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void incorrectDeleteUrlTest() throws Exception {
        String url = "/incorrect_url";

        mvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void incorrectGetUrlTest() throws Exception {
        String url = "/incorrect_url";

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void incorrectPutUrlTest() throws Exception {
        String url = "/incorrect_url";

        mvc.perform(MockMvcRequestBuilders.put(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void incorrectPatchUrlTest() throws Exception {
        String url = "/incorrect_url";

        mvc.perform(MockMvcRequestBuilders.patch(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void requestWithIncorrectSessionIdTest() throws Exception {
        String incorrectSessionId = UUID.randomUUID().toString();
        String url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.DOCTOR_REGISTRATION_URL);
        DoctorRegistrationDtoRequest request = new DoctorRegistrationDtoRequest(
                LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, Collections.emptyList(),
                "Епихария", "Козлова", null,
                "Surgeon", "124", "EpihariyaKozlova75", "44XNaexggtgK");
        String json = mapToJson(request);

        String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, incorrectSessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        ErrorsDtoResponse expectedResponse =
                new ErrorsDtoResponse(new ErrorDtoResponse(
                        PermissionDeniedErrorCode.PERMISSION_DENIED.getErrorCode(),
                        CookieFactory.JAVA_SESSION_ID,
                        PermissionDeniedErrorCode.PERMISSION_DENIED.getErrorMessage()));
        ErrorsDtoResponse actualResponse = mapFromJson(actualJsonResponse, expectedResponse.getClass());
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void incorrectRequestTest1() throws Exception {
        String rootAdminSessionId = loginRootAdmin();
        String url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.DOCTOR_REGISTRATION_URL);
        DoctorRegistrationDtoRequest request =
                new DoctorRegistrationDtoRequest(null, "fsdafas", -1, Collections.emptyList(),
                        null, "", null,
                        "", null, "", null);
        String json = mapToJson(request);

        String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, rootAdminSessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        ErrorsDtoResponse expectedResponse = new ErrorsDtoResponse(
                genErrorResponse.apply("INVALID_NAME", "firstName"),
                genErrorResponse.apply("INVALID_NAME", "lastName"),
                genErrorResponse.apply("INVALID_SPECIALITY", "speciality"),
                genErrorResponse.apply("INVALID_ROOM", "room"),
                genErrorResponse.apply("INVALID_LOGIN", "login"),
                genErrorResponse.apply("INVALID_PASSWORD", "password"),
                genErrorResponse.apply("INVALID_DATE", "dateStart"),
                genErrorResponse.apply("INVALID_DATE", "dateEnd"),
                genErrorResponse.apply("INVALID_DURATION", "duration")
        );
        expectedResponse.getErrors().sort(Comparator.comparing(ErrorDtoResponse::getField));

        ErrorsDtoResponse actualResponse = mapFromJson(actualJsonResponse, expectedResponse.getClass());
        actualResponse.getErrors().sort(Comparator.comparing(ErrorDtoResponse::getField));

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void incorrectRequestTest2() throws Exception {
        String rootAdminSessionId = loginRootAdmin();
        String url = buildUrl(AdministratorController.PREFIX_URL, AdministratorController.DOCTOR_REGISTRATION_URL);
        DoctorRegistrationDtoRequest request =
                new DoctorRegistrationDtoRequest(null, "fsdafas", -1, Collections.emptyList(),
                        "adasfas2#", "2#", "sad#2",
                        "", null, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "asd");
        String json = mapToJson(request);

        String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, rootAdminSessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        ErrorsDtoResponse expectedResponse = new ErrorsDtoResponse(
                genErrorResponse.apply("INVALID_NAME", "firstName"),
                genErrorResponse.apply("INVALID_NAME", "lastName"),
                genErrorResponse.apply("INVALID_NAME", "patronymic"),
                genErrorResponse.apply("INVALID_SPECIALITY", "speciality"),
                genErrorResponse.apply("INVALID_ROOM", "room"),
                genErrorResponse.apply("INVALID_LOGIN", "login"),
                genErrorResponse.apply("INVALID_PASSWORD", "password"),
                genErrorResponse.apply("INVALID_DATE", "dateStart"),
                genErrorResponse.apply("INVALID_DATE", "dateEnd"),
                genErrorResponse.apply("INVALID_DURATION", "duration")
        );
        expectedResponse.getErrors().sort(Comparator.comparing(ErrorDtoResponse::getField));

        ErrorsDtoResponse actualResponse = mapFromJson(actualJsonResponse, expectedResponse.getClass());
        actualResponse.getErrors().sort(Comparator.comparing(ErrorDtoResponse::getField));

        Assert.assertEquals(expectedResponse, actualResponse);
    }
}