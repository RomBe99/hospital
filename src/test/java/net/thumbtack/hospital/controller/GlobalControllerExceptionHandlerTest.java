package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.other.ErrorDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ErrorsDtoResponse;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@RunWith(SpringRunner.class)
public class GlobalControllerExceptionHandlerTest extends ControllerTestApi {
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
        String url = AdministratorController.PREFIX_URL + "/" + AdministratorController.DOCTOR_REGISTRATION_URL;
        DoctorRegistrationDtoRequest request =
                new DoctorRegistrationDtoRequest(LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, new ArrayList<>(),
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
}