package net.thumbtack.hospital.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.hospital.server.HospitalApplication;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.StringJoiner;

@SpringBootTest(classes = HospitalApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseSpringConfiguration implements UserTestApi, PatientTestApi, AdministratorTestApi, DoctorTestApi {
    @Autowired
    private ObjectMapper jsonMapper;

    @BeforeClass
    public static void setUpDatabase() {
        MyBatisUtils.initConnection();
    }

    public String buildUrl(String... urlParts) {
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

    public String buildUrlWithPathVariable(String pathVarName, String pathVarValue, String... urlParts) {
        final String nameWithBrackets = '{' + pathVarName + '}';

        return buildUrl(urlParts).replace(nameWithBrackets, pathVarValue);
    }

    public String mapToJson(Object obj) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(obj);
    }

    public <T> T mapFromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return jsonMapper.readValue(json, clazz);
    }
}