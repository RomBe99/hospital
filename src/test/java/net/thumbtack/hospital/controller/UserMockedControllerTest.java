package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.configuration.Constraints;
import net.thumbtack.hospital.controller.api.MockedControllerTestApi;
import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ServerSettingsDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.SettingsDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.util.ScheduleGenerators;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@RunWith(SpringRunner.class)
public class UserMockedControllerTest extends MockedControllerTestApi {
    @Autowired
    private Constraints constraints;
    private static final Function<Constraints, ServerSettingsDtoResponse> serverSettingsProducer =
            c -> new ServerSettingsDtoResponse(c.getMaxNameLength(), c.getMinPasswordLength());

    @Test
    public void getAdministratorSettings() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        final SettingsDtoResponse expectedResponse = serverSettingsProducer.apply(constraints);
        final SettingsDtoResponse actualResponse = getSettings(rootAdminSessionId, expectedResponse.getClass());
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void getDoctorSettings() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        final int duration = 15;
        final LocalDate dateStart = LocalDate.of(2020, 3, 1);
        final LocalDate dateEnd = LocalDate.of(2020, 3, 31);
        final LocalTime timeStart = LocalTime.of(8, 0);
        final LocalTime timeEnd = LocalTime.of(17, 0);
        final List<Integer> weekDays = Arrays.asList(1, 2, 3);

        final DtoRequestWithSchedule generatedWeekSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(
                duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final DoctorRegistrationDtoRequest doctorRegistrationRequest = new DoctorRegistrationDtoRequest(
                generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                generatedWeekSchedule.getWeekSchedule(),
                "Саркис", "Семёнов", "Вениаминович",
                "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");

        final DoctorRegistrationDtoResponse actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
        final DoctorRegistrationDtoResponse expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
        Assert.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);

        final Pair<String, DoctorLoginDtoResponse> loginData = login(doctorRegistrationRequest.getLogin(), doctorRegistrationRequest.getPassword(), DoctorLoginDtoResponse.class);

        {
            final DoctorLoginDtoResponse actualResponse = loginData.getValue();
            final DoctorLoginDtoResponse expectedResponse = new DoctorLoginDtoResponse(actualResponse.getId(),
                    expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                    expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), expectedDoctorRegistrationResponse.getSchedule());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        {
            final SettingsDtoResponse expectedResponse = serverSettingsProducer.apply(constraints);
            final SettingsDtoResponse actualResponse = getSettings(loginData.getKey(), expectedResponse.getClass());

            Assert.assertEquals(expectedResponse, actualResponse);
        }
    }

    @Test
    public void getPatientSettings() throws Exception {
        final PatientRegistrationDtoRequest patientRegistrationRequest = new PatientRegistrationDtoRequest(
                "Пахон", "Петров",
                "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                "PahonPetrov927", "ugSfPaGD1YBv");

        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);

        {
            final PatientRegistrationDtoResponse actualResponse = patientRegistrationData.getValue();
            final PatientRegistrationDtoResponse expectedResponse = new PatientRegistrationDtoResponse(
                    actualResponse.getId(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        {
            final SettingsDtoResponse expectedResponse = serverSettingsProducer.apply(constraints);
            final SettingsDtoResponse actualResponse = getSettings(patientRegistrationData.getKey(), expectedResponse.getClass());

            Assert.assertEquals(expectedResponse, actualResponse);
        }
    }
}