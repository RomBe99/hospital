package net.thumbtack.hospital.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.hospital.server.HospitalApplication;
import net.thumbtack.hospital.util.ScheduleGenerator;
import net.thumbtack.hospital.util.adapter.DtoAdapters;
import net.thumbtack.hospital.util.adapter.ScheduleTransformer;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.StringJoiner;

@SpringBootTest(classes = HospitalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseSpringConfiguration implements UserTestApi, PatientTestApi, AdministratorTestApi, DoctorTestApi {
    @Autowired
    private ObjectMapper jsonMapper;
    @Autowired
    private TicketFactory ticketFactory;
    @Autowired
    private ScheduleTransformer scheduleTransformer;
    @Autowired
    private DtoAdapters dtoAdapters;
    @Autowired
    private ScheduleGenerator scheduleGenerator;

    @BeforeAll
    public static void setUpDatabase() {
        MyBatisUtils.initConnection();
    }

    public String buildUrl(String... urlParts) {
        final var separator = "/";

        if (urlParts.length == 0) {
            return separator;
        }

        final var sj = new StringJoiner(separator);

        for (var part : urlParts) {
            sj.add(part);
        }

        final var result = sj.toString();

        return result.startsWith(separator) ? result : separator + result;
    }

    public String buildUrlWithPathVariable(String pathVarName, String pathVarValue, String... urlParts) {
        return buildUrl(urlParts).replace(String.format("{%s}", pathVarName), pathVarValue);
    }

    public String mapToJson(Object obj) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(obj);
    }

    public <T> T mapFromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return jsonMapper.readValue(json, clazz);
    }

    public TicketFactory getTicketFactory() {
        return ticketFactory;
    }

    public ScheduleTransformer getScheduleTransformer() {
        return scheduleTransformer;
    }

    public DtoAdapters getDtoAdapters() {
        return dtoAdapters;
    }

    public ScheduleGenerator getScheduleGenerator() {
        return scheduleGenerator;
    }
}