package net.thumbtack.hospital.controller.integration;

import net.thumbtack.hospital.configuration.Constraints;
import net.thumbtack.hospital.controller.api.RealControllerTestApi;
import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ServerSettingsDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.util.ScheduleGenerators;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class UserControllerTest extends RealControllerTestApi {
    @Autowired
    private Constraints constraints;

    @Test
    public void getAdministratorSettings() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();
        final var correctServerSettingsResponse =
                new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength());

        final var actualResponse = getSettings(rootAdminSessionId, correctServerSettingsResponse.getClass());
        Assertions.assertEquals(correctServerSettingsResponse, actualResponse);
    }

    @Test
    public void getDoctorSettings() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        final var duration = 15;
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = LocalDate.of(2020, 3, 31);
        final var timeStart = LocalTime.of(8, 0);
        final var timeEnd = LocalTime.of(17, 0);
        final var weekDays = List.of(1, 2, 3);

        final var generatedWeekSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(
                duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final var doctorRegistrationRequest = new DoctorRegistrationDtoRequest(
                generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                generatedWeekSchedule.getWeekSchedule(),
                "Саркис", "Семёнов", "Вениаминович",
                "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");

        final var actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
        final var expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
        Assertions.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);

        final var loginData = login(doctorRegistrationRequest.getLogin(), doctorRegistrationRequest.getPassword(), DoctorLoginDtoResponse.class);

        {
            final var actualResponse = loginData.getValue();
            final var expectedResponse = new DoctorLoginDtoResponse(actualResponse.getId(),
                    expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                    expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), expectedDoctorRegistrationResponse.getSchedule());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        {
            final var correctServerSettingsResponse =
                    new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength());
            final var actualResponse = getSettings(loginData.getKey(), correctServerSettingsResponse.getClass());

            Assertions.assertEquals(correctServerSettingsResponse, actualResponse);
        }
    }

    @Test
    public void getPatientSettings() {
        final var patientRegistrationRequest = new PatientRegistrationDtoRequest(
                "Пахон", "Петров",
                "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                "PahonPetrov927", "ugSfPaGD1YBv");

        final var patientRegistrationData = patientRegistration(patientRegistrationRequest);

        {
            final var actualResponse = patientRegistrationData.getValue();
            final var expectedResponse = new PatientRegistrationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        {
            final var correctServerSettingsResponse =
                    new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength());
            final var actualResponse = getSettings(patientRegistrationData.getKey(), correctServerSettingsResponse.getClass());

            Assertions.assertEquals(correctServerSettingsResponse, actualResponse);
        }
    }
}