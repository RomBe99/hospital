package net.thumbtack.hospital.controller.integration;

import net.thumbtack.hospital.controller.api.RealControllerTestApi;
import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.*;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.util.ScheduleGenerator;
import net.thumbtack.hospital.util.WeekDay;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class AdministratorControllerTest extends RealControllerTestApi {
    @Test
    public void administratorRegistrationTest() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();
        final var registrationRequest =
                new AdminRegistrationDtoRequest("Аласдаир", "Охота", "Test admin №1", "OhotaAlasdair252", "SDQGjC4GUvAB");

        {
            final var actualResponse = administratorRegistration(rootAdminSessionId, registrationRequest);
            final var expectedResponse = new AdminRegistrationDtoResponse(actualResponse.getId(),
                    registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(),
                    registrationRequest.getPosition());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        final var loginData = login(registrationRequest.getLogin(), registrationRequest.getPassword(), AdminLoginDtoResponse.class);
        final var testAdminSessionId = loginData.getKey();

        {
            final var actualResponse = loginData.getValue();
            final var expectedResponse = new AdminLoginDtoResponse(actualResponse.getId(), registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(), registrationRequest.getPosition());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        {
            final var actualResponse = getUserInformation(testAdminSessionId, AdminInformationDtoResponse.class);
            final var expectedResponse = new AdminInformationDtoResponse(actualResponse.getId(), registrationRequest.getLogin(), registrationRequest.getPassword(),
                    registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(),
                    registrationRequest.getPosition());
            expectedResponse.setId(actualResponse.getId());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        logout(rootAdminSessionId);
        logout(testAdminSessionId);
    }

    @Test
    public void editAdministratorProfileTest() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();
        final var registrationRequest = new AdminRegistrationDtoRequest("Аласдаир", "Охота", "Test admin №1", "OhotaAlasdair252", "SDQGjC4GUvAB");

        {
            final var actualResponse = administratorRegistration(rootAdminSessionId, registrationRequest);
            final var expectedResponse = new AdminRegistrationDtoResponse(actualResponse.getId(),
                    registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(),
                    registrationRequest.getPosition());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        final var loginData = login(registrationRequest.getLogin(), registrationRequest.getPassword(), AdminLoginDtoResponse.class);
        final var testAdminSessionId = loginData.getKey();
        final var adminLoginResponse = new AdminLoginDtoResponse(loginData.getValue().getId(),
                registrationRequest.getFirstName(), registrationRequest.getLastName(),
                registrationRequest.getPatronymic(), registrationRequest.getPosition());
        Assertions.assertEquals(adminLoginResponse, loginData.getValue());

        final var editAdminProfileRequest =
                new EditAdminProfileDtoRequest("Алимад", "Федоров",
                        registrationRequest.getPosition(), registrationRequest.getPassword(), "OMoqtge7e1V1");

        {
            final var actualResponse = editAdministratorProfile(testAdminSessionId, editAdminProfileRequest);
            final var expectedResponse = new EditAdminProfileDtoResponse(adminLoginResponse.getId(),
                    editAdminProfileRequest.getFirstName(), editAdminProfileRequest.getLastName(), editAdminProfileRequest.getPatronymic(), editAdminProfileRequest.getPosition());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }


        {
            final var expectedResponse = new AdminInformationDtoResponse(
                    registrationRequest.getLogin(), editAdminProfileRequest.getNewPassword(),
                    editAdminProfileRequest.getFirstName(), editAdminProfileRequest.getLastName(), editAdminProfileRequest.getPatronymic(),
                    editAdminProfileRequest.getPosition());
            final var actualResponse = getUserInformation(testAdminSessionId, expectedResponse.getClass());
            expectedResponse.setId(actualResponse.getId());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        logout(rootAdminSessionId);
        logout(testAdminSessionId);
    }

    @Test
    public void removeDoctorTest() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        final var dateStart = LocalDate.now();
        final var dateEnd = dateStart.plusDays(5);


        final var doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(dateStart.toString(), dateEnd.toString(), 15, Collections.emptyList(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        final var actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
        final var expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
        Assertions.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);

        {
            final var removeDoctorRequest = new RemoveDoctorDtoRequest(LocalDate.now().toString());
            final var actualResponse = removeDoctor(rootAdminSessionId, expectedDoctorRegistrationResponse.getId(), removeDoctorRequest);
            final var expectedResponse = new EmptyDtoResponse();
            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        logout(rootAdminSessionId);
    }

    @Test
    public void getPatientInformationByAdminAndDoctorTest() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        final var patientRegistrationRequest = new PatientRegistrationDtoRequest(
                "Аббо", "Титов", "vixej10213@inmail92.com",
                "399822, г. Дербент, ул. Онежская, дом 77, квартира 773", "+7 (922) 069-47-76",
                "AbboTitov756", "zRAMIyf7uUVc");
        final var patientRegistrationData = patientRegistration(patientRegistrationRequest);
        final var patientRegistrationResponse = new PatientRegistrationDtoResponse(patientRegistrationData.getValue().getId(),
                patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

        final var expectedResponse = new PatientInformationDtoResponse(patientRegistrationResponse.getId(),
                patientRegistrationResponse.getFirstName(), patientRegistrationResponse.getLastName(), patientRegistrationResponse.getPatronymic(),
                patientRegistrationResponse.getEmail(), patientRegistrationResponse.getAddress(), patientRegistrationResponse.getPhone());

        {
            final var actualResponse = getPatientInformation(rootAdminSessionId, expectedResponse.getId());
            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        final var doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(LocalDate.now().toString(), LocalDate.now().toString(), 15, Collections.emptyList(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        final var actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
        final var expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
        Assertions.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);

        final var loginData = login(doctorRegistrationRequest.getLogin(), doctorRegistrationRequest.getPassword(), DoctorLoginDtoResponse.class);

        {
            final var actualDoctorLoginResponse = loginData.getValue();
            final var expectedDoctorLoginResponse = new DoctorLoginDtoResponse(expectedDoctorRegistrationResponse.getId(),
                    expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                    expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), expectedDoctorRegistrationResponse.getSchedule());

            Assertions.assertEquals(expectedDoctorLoginResponse, actualDoctorLoginResponse);
        }

        final var doctorSessionId = loginData.getKey();

        {
            final var actualResponse = getPatientInformation(doctorSessionId, patientRegistrationResponse.getId());
            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        logout(doctorSessionId);
        logout(patientRegistrationData.getKey());
        logout(rootAdminSessionId);
    }

    @Test
    public void insertDoctorWithWeekScheduleTest() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        final var duration = 15;
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = LocalDate.of(2020, 3, 31);
        final var timeStart = LocalTime.of(8, 0);
        final var timeEnd = LocalTime.of(17, 0);
        final var weekDays = List.of(1, 2, 3);

        final var generatedWeekSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(
                duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final var doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
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
            final var actualDoctorLoginResponse = loginData.getValue();
            final var expectedDoctorLoginResponse = new DoctorLoginDtoResponse(actualDoctorLoginResponse.getId(),
                    expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                    expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), expectedDoctorRegistrationResponse.getSchedule());

            Assertions.assertEquals(expectedDoctorLoginResponse, actualDoctorLoginResponse);
        }

        logout(loginData.getKey());
        logout(rootAdminSessionId);
    }

    @Test
    public void insertDoctorWithWeekDayScheduleTest() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        var duration = 30;
        var dateStart = LocalDate.of(2020, 7, 1);
        var dateEnd = LocalDate.of(2020, 8, 15);
        var weekSchedule = new HashMap<WeekDay, Pair<LocalTime, LocalTime>>();
        weekSchedule.put(WeekDay.MONDAY, Pair.of(LocalTime.of(8, 0), LocalTime.of(14, 0)));
        weekSchedule.put(WeekDay.TUESDAY, Pair.of(LocalTime.of(12, 0), LocalTime.of(18, 0)));
        weekSchedule.put(WeekDay.THURSDAY, Pair.of(LocalTime.of(10, 0), LocalTime.of(16, 0)));

        final var generatedWeekSchedule =
                getScheduleGenerator().generateDtoRequestWithDaySchedule(duration, dateStart, dateEnd, weekSchedule);

        final var doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                        generatedWeekSchedule.getWeekDaysSchedule(),
                        "Юнонна", "Ушакова", "Константиновна",
                        "Dentist", "471", "YunonnaUshakova642", "qypzb2XheqYG");
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

        logout(rootAdminSessionId);
        logout(loginData.getKey());
    }

    @Test
    public void editDoctorScheduleWithWeekScheduleTest() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        var duration = 15;
        var dateStart = LocalDate.of(2020, 3, 1);
        var dateEnd = LocalDate.of(2020, 3, 31);
        var timeStart = LocalTime.of(8, 0);
        var timeEnd = LocalTime.of(17, 0);
        var weekDays = List.of(1, 2, 3);

        var generatedWeekSchedule =
                getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final var doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                        generatedWeekSchedule.getWeekSchedule(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        final var actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
        final var expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
        Assertions.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);

        duration = 30;
        dateEnd = dateStart.plusDays(15);
        timeStart = LocalTime.of(10, 0);
        timeEnd = LocalTime.of(16, 30);
        weekDays = List.of(1, 3, 4, 5);
        generatedWeekSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(
                duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);
        final var doctorId = expectedDoctorRegistrationResponse.getId();
        final var expectedSchedule =
                getDtoAdapters().transform(getScheduleTransformer().transformWeekSchedule(generatedWeekSchedule, doctorId));

        {
            final var editDoctorScheduleRequest = new EditDoctorScheduleDtoRequest(
                    dateStart.toString(), dateEnd.toString(), duration, generatedWeekSchedule.getWeekSchedule());
            final var expectedResponse = new EditDoctorScheduleDtoResponse(
                    doctorId,
                    expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(),
                    expectedDoctorRegistrationResponse.getPatronymic(), expectedDoctorRegistrationResponse.getSpeciality(),
                    expectedDoctorRegistrationResponse.getRoom(), expectedSchedule);
            final var actualResponse = editDoctorSchedule(rootAdminSessionId, doctorId, editDoctorScheduleRequest);

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        logout(rootAdminSessionId);
    }

    @Test
    public void editDoctorScheduleWithWeekDayScheduleTest() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        var duration = 30;
        var dateStart = LocalDate.of(2020, 7, 1);
        var dateEnd = LocalDate.of(2020, 8, 15);
        var weekSchedule = new HashMap<WeekDay, Pair<LocalTime, LocalTime>>();
        weekSchedule.put(WeekDay.MONDAY, Pair.of(LocalTime.of(8, 0), LocalTime.of(14, 0)));
        weekSchedule.put(WeekDay.TUESDAY, Pair.of(LocalTime.of(12, 0), LocalTime.of(18, 0)));
        weekSchedule.put(WeekDay.FRIDAY, Pair.of(LocalTime.of(10, 0), LocalTime.of(16, 0)));

        var generatedWeekSchedule =
                getScheduleGenerator().generateDtoRequestWithDaySchedule(duration, dateStart, dateEnd, weekSchedule);

        final var doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                        generatedWeekSchedule.getWeekDaysSchedule(),
                        "Юнонна", "Ушакова", "Константиновна",
                        "Dentist", "471", "YunonnaUshakova642", "qypzb2XheqYG");
        final var actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
        final var expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
        Assertions.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);

        duration = 15;
        dateStart = dateStart.plusDays(5);
        dateEnd = dateEnd.minusDays(10);
        weekSchedule.clear();
        weekSchedule.put(WeekDay.MONDAY, Pair.of(LocalTime.of(12, 30), LocalTime.of(18, 0)));
        weekSchedule.put(WeekDay.WEDNESDAY, Pair.of(LocalTime.of(11, 15), LocalTime.of(17, 45)));
        weekSchedule.put(WeekDay.THURSDAY, Pair.of(LocalTime.of(8, 45), LocalTime.of(14, 15)));
        weekSchedule.put(WeekDay.FRIDAY, Pair.of(LocalTime.of(10, 30), LocalTime.of(16, 0)));
        generatedWeekSchedule =
                getScheduleGenerator().generateDtoRequestWithDaySchedule(duration, dateStart, dateEnd, weekSchedule);
        final var doctorId = expectedDoctorRegistrationResponse.getId();
        final var expectedSchedule =
                getDtoAdapters().transform(getScheduleTransformer().transformWeekDaysSchedule(generatedWeekSchedule, doctorId));

        {
            final var editDoctorScheduleRequest = new EditDoctorScheduleDtoRequest(
                    dateStart.toString(), dateEnd.toString(), duration, generatedWeekSchedule.getWeekDaysSchedule());
            final var expectedResponse = new EditDoctorScheduleDtoResponse(
                    doctorId,
                    expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(),
                    expectedDoctorRegistrationResponse.getPatronymic(), expectedDoctorRegistrationResponse.getSpeciality(),
                    expectedDoctorRegistrationResponse.getRoom(), expectedSchedule);
            final var actualResponse = editDoctorSchedule(rootAdminSessionId, doctorId, editDoctorScheduleRequest);

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        logout(rootAdminSessionId);
    }
}