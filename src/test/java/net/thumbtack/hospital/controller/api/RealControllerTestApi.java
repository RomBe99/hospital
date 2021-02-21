package net.thumbtack.hospital.controller.api;

import net.thumbtack.hospital.controller.UserController;
import net.thumbtack.hospital.dtorequest.user.LoginDtoRequest;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public abstract class RealControllerTestApi extends BaseSpringConfiguration {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Override
    public <T extends LoginUserDtoResponse> Pair<String, T> login(String login, String password, Class<T> clazz) throws Exception {
        final String url = buildUrl(UserController.PREFIX_URL, UserController.LOGIN_URL);
        final LoginDtoRequest request = new LoginDtoRequest(login, password);
        final ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, request, clazz);

        // FIXME Сделать правильное извлечение айдишника ссессии
        final String sessionId = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        Assert.assertTrue(Objects.requireNonNull(sessionId).isEmpty());

        final T actualResponse = responseEntity.getBody();
        Assert.assertNotEquals(0, Objects.requireNonNull(actualResponse).getId());

        return Pair.of(sessionId, actualResponse);
    }
}