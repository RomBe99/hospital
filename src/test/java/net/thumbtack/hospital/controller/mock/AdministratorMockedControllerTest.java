package net.thumbtack.hospital.controller.mock;

import net.thumbtack.hospital.controller.api.MockedControllerTestApi;
import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.admin.*;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;
import net.thumbtack.hospital.util.ScheduleGenerators;
import net.thumbtack.hospital.util.WeekDay;
import net.thumbtack.hospital.util.adapter.DtoAdapters;
import net.thumbtack.hospital.util.adapter.ScheduleTransformers;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RunWith(SpringRunner.class)
public class AdministratorMockedControllerTest extends MockedControllerTestApi {
    @Test
    public void administratorRegistrationTest() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();
        final AdminRegistrationDtoRequest registrationRequest =
                new AdminRegistrationDtoRequest("Аласдаир", "Охота", "Test admin №1", "OhotaAlasdair252", "SDQGjC4GUvAB");

        {
            final AdminRegistrationDtoResponse actualResponse = administratorRegistration(rootAdminSessionId, registrationRequest);
            final AdminRegistrationDtoResponse expectedResponse = new AdminRegistrationDtoResponse(actualResponse.getId(),
                    registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(),
                    registrationRequest.getPosition());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final Pair<String, AdminLoginDtoResponse> loginData = login(registrationRequest.getLogin(), registrationRequest.getPassword(), AdminLoginDtoResponse.class);
        final String testAdminSessionId = loginData.getKey();

        {
            final AdminLoginDtoResponse actualResponse = loginData.getValue();
            final AdminLoginDtoResponse expectedResponse = new AdminLoginDtoResponse(actualResponse.getId(), registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(), registrationRequest.getPosition());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        {
            final AdminInformationDtoResponse actualResponse = getUserInformation(testAdminSessionId, AdminInformationDtoResponse.class);
            final AdminInformationDtoResponse expectedResponse = new AdminInformationDtoResponse(actualResponse.getId(), registrationRequest.getLogin(), registrationRequest.getPassword(),
                    registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(),
                    registrationRequest.getPosition());
            expectedResponse.setId(actualResponse.getId());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        logout(rootAdminSessionId);
        logout(testAdminSessionId);
    }

    @Test
    public void editAdministratorProfileTest() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();
        final AdminRegistrationDtoRequest registrationRequest =
                new AdminRegistrationDtoRequest("Аласдаир", "Охота", "Test admin №1", "OhotaAlasdair252", "SDQGjC4GUvAB");

        {
            final AdminRegistrationDtoResponse actualResponse = administratorRegistration(rootAdminSessionId, registrationRequest);
            final AdminRegistrationDtoResponse expectedResponse = new AdminRegistrationDtoResponse(actualResponse.getId(),
                    registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(),
                    registrationRequest.getPosition());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final Pair<String, AdminLoginDtoResponse> loginData = login(registrationRequest.getLogin(), registrationRequest.getPassword(), AdminLoginDtoResponse.class);
        final String testAdminSessionId = loginData.getKey();
        final AdminLoginDtoResponse adminLoginResponse = new AdminLoginDtoResponse(loginData.getValue().getId(),
                registrationRequest.getFirstName(), registrationRequest.getLastName(),
                registrationRequest.getPatronymic(), registrationRequest.getPosition());
        Assert.assertEquals(adminLoginResponse, loginData.getValue());

        final EditAdminProfileDtoRequest editAdminProfileRequest =
                new EditAdminProfileDtoRequest("Алимад", "Федоров",
                        registrationRequest.getPosition(), registrationRequest.getPassword(), "OMoqtge7e1V1");

        {
            final EditAdminProfileDtoResponse actualResponse = editAdministratorProfile(testAdminSessionId, editAdminProfileRequest);
            final EditAdminProfileDtoResponse expectedResponse = new EditAdminProfileDtoResponse(adminLoginResponse.getId(),
                    editAdminProfileRequest.getFirstName(), editAdminProfileRequest.getLastName(), editAdminProfileRequest.getPatronymic(), editAdminProfileRequest.getPosition());

            Assert.assertEquals(expectedResponse, actualResponse);
        }


        {
            final AdminInformationDtoResponse expectedResponse = new AdminInformationDtoResponse(
                    registrationRequest.getLogin(), editAdminProfileRequest.getNewPassword(),
                    editAdminProfileRequest.getFirstName(), editAdminProfileRequest.getLastName(), editAdminProfileRequest.getPatronymic(),
                    editAdminProfileRequest.getPosition());
            final AdminInformationDtoResponse actualResponse = getUserInformation(testAdminSessionId, expectedResponse.getClass());
            expectedResponse.setId(actualResponse.getId());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        logout(rootAdminSessionId);
        logout(testAdminSessionId);
    }

    @Test
    public void removeDoctorTest() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        final LocalDate dateStart = LocalDate.now();
        final LocalDate dateEnd = dateStart.plusDays(5);


        final DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(dateStart.toString(), dateEnd.toString(), 15, Collections.emptyList(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        final DoctorRegistrationDtoResponse actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
        final DoctorRegistrationDtoResponse expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
        Assert.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);

        {
            final RemoveDoctorDtoRequest removeDoctorRequest = new RemoveDoctorDtoRequest(LocalDate.now().toString());
            final EmptyDtoResponse actualResponse = removeDoctor(rootAdminSessionId, expectedDoctorRegistrationResponse.getId(), removeDoctorRequest);
            final EmptyDtoResponse expectedResponse = new EmptyDtoResponse();
            Assert.assertEquals(expectedResponse, actualResponse);
        }

        logout(rootAdminSessionId);
    }

    @Test
    public void getPatientInformationByAdminAndDoctorTest() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        final PatientRegistrationDtoRequest patientRegistrationRequest = new PatientRegistrationDtoRequest(
                "Аббо", "Титов", "vixej10213@inmail92.com",
                "399822, г. Дербент, ул. Онежская, дом 77, квартира 773", "+7 (922) 069-47-76",
                "AbboTitov756", "zRAMIyf7uUVc");
        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);
        final PatientRegistrationDtoResponse patientRegistrationResponse = new PatientRegistrationDtoResponse(patientRegistrationData.getValue().getId(),
                patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

        final PatientInformationDtoResponse expectedResponse = new PatientInformationDtoResponse(patientRegistrationResponse.getId(),
                patientRegistrationResponse.getFirstName(), patientRegistrationResponse.getLastName(), patientRegistrationResponse.getPatronymic(),
                patientRegistrationResponse.getEmail(), patientRegistrationResponse.getAddress(), patientRegistrationResponse.getPhone());

        {
            final PatientInformationDtoResponse actualResponse = getPatientInformation(rootAdminSessionId, expectedResponse.getId());
            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(LocalDate.now().toString(), LocalDate.now().toString(), 15, Collections.emptyList(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        final DoctorRegistrationDtoResponse actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
        final DoctorRegistrationDtoResponse expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
        Assert.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);

        final Pair<String, DoctorLoginDtoResponse> loginData = login(doctorRegistrationRequest.getLogin(), doctorRegistrationRequest.getPassword(), DoctorLoginDtoResponse.class);

        {
            final DoctorLoginDtoResponse actualDoctorLoginResponse = loginData.getValue();
            final DoctorLoginDtoResponse expectedDoctorLoginResponse = new DoctorLoginDtoResponse(expectedDoctorRegistrationResponse.getId(),
                    expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                    expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), expectedDoctorRegistrationResponse.getSchedule());

            Assert.assertEquals(expectedDoctorLoginResponse, actualDoctorLoginResponse);
        }

        final String doctorSessionId = loginData.getKey();

        {
            final PatientInformationDtoResponse actualResponse = getPatientInformation(doctorSessionId, patientRegistrationResponse.getId());
            Assert.assertEquals(expectedResponse, actualResponse);
        }

        logout(doctorSessionId);
        logout(patientRegistrationData.getKey());
        logout(rootAdminSessionId);
    }

    @Test
    public void insertDoctorWithWeekScheduleTest() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        final int duration = 15;
        final LocalDate dateStart = LocalDate.of(2020, 3, 1);
        final LocalDate dateEnd = LocalDate.of(2020, 3, 31);
        final LocalTime timeStart = LocalTime.of(8, 0);
        final LocalTime timeEnd = LocalTime.of(17, 0);
        final List<Integer> weekDays = Arrays.asList(1, 2, 3);

        final DtoRequestWithSchedule generatedWeekSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(
                duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
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
            final DoctorLoginDtoResponse actualDoctorLoginResponse = loginData.getValue();
            final DoctorLoginDtoResponse expectedDoctorLoginResponse = new DoctorLoginDtoResponse(actualDoctorLoginResponse.getId(),
                    expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                    expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), expectedDoctorRegistrationResponse.getSchedule());

            Assert.assertEquals(expectedDoctorLoginResponse, actualDoctorLoginResponse);
        }

        logout(loginData.getKey());
        logout(rootAdminSessionId);
    }

    @Test
    public void insertDoctorWithWeekDayScheduleTest() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        int duration = 30;
        LocalDate dateStart = LocalDate.of(2020, 7, 1);
        LocalDate dateEnd = LocalDate.of(2020, 8, 15);
        Map<WeekDay, AbstractMap.SimpleEntry<LocalTime, LocalTime>> weekSchedule = new HashMap<>();
        weekSchedule.put(WeekDay.MONDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(8, 0), LocalTime.of(14, 0)));
        weekSchedule.put(WeekDay.TUESDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(12, 0), LocalTime.of(18, 0)));
        weekSchedule.put(WeekDay.THURSDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(10, 0), LocalTime.of(16, 0)));

        final DtoRequestWithSchedule generatedWeekSchedule =
                ScheduleGenerators.generateDtoRequestWithDaySchedule(duration, dateStart, dateEnd, weekSchedule);

        final DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                        generatedWeekSchedule.getWeekDaysSchedule(),
                        "Юнонна", "Ушакова", "Константиновна",
                        "Dentist", "471", "YunonnaUshakova642", "qypzb2XheqYG");
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

        logout(rootAdminSessionId);
        logout(loginData.getKey());
    }

    @Test
    public void editDoctorScheduleWithWeekScheduleTest() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        int duration = 15;
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = LocalDate.of(2020, 3, 31);
        LocalTime timeStart = LocalTime.of(8, 0);
        LocalTime timeEnd = LocalTime.of(17, 0);
        List<Integer> weekDays = Arrays.asList(1, 2, 3);

        DtoRequestWithSchedule generatedWeekSchedule =
                ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                        generatedWeekSchedule.getWeekSchedule(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        final DoctorRegistrationDtoResponse actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
        final DoctorRegistrationDtoResponse expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
        Assert.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);

        duration = 30;
        dateEnd = dateStart.plusDays(15);
        timeStart = LocalTime.of(10, 0);
        timeEnd = LocalTime.of(16, 30);
        weekDays = Arrays.asList(1, 3, 4, 5);
        generatedWeekSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(
                duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);
        final int doctorId = expectedDoctorRegistrationResponse.getId();
        final List<ScheduleCellDtoResponse> expectedSchedule =
                DtoAdapters.transform(ScheduleTransformers.transformWeekSchedule(generatedWeekSchedule, doctorId));

        {
            final EditDoctorScheduleDtoRequest editDoctorScheduleRequest =
                    new EditDoctorScheduleDtoRequest(dateStart.toString(), dateEnd.toString(), duration, generatedWeekSchedule.getWeekSchedule());
            final EditDoctorScheduleDtoResponse expectedResponse =
                    new EditDoctorScheduleDtoResponse(doctorId,
                            expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(),
                            expectedDoctorRegistrationResponse.getPatronymic(), expectedDoctorRegistrationResponse.getSpeciality(),
                            expectedDoctorRegistrationResponse.getRoom(), expectedSchedule);
            final EditDoctorScheduleDtoResponse actualResponse = editDoctorSchedule(rootAdminSessionId, doctorId, editDoctorScheduleRequest);

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        logout(rootAdminSessionId);
    }

    @Test
    public void editDoctorScheduleWithWeekDayScheduleTest() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        int duration = 30;
        LocalDate dateStart = LocalDate.of(2020, 7, 1);
        LocalDate dateEnd = LocalDate.of(2020, 8, 15);
        Map<WeekDay, AbstractMap.SimpleEntry<LocalTime, LocalTime>> weekSchedule = new HashMap<>();
        weekSchedule.put(WeekDay.MONDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(8, 0), LocalTime.of(14, 0)));
        weekSchedule.put(WeekDay.TUESDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(12, 0), LocalTime.of(18, 0)));
        weekSchedule.put(WeekDay.FRIDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(10, 0), LocalTime.of(16, 0)));

        DtoRequestWithSchedule generatedWeekSchedule =
                ScheduleGenerators.generateDtoRequestWithDaySchedule(duration, dateStart, dateEnd, weekSchedule);

        final DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                        generatedWeekSchedule.getWeekDaysSchedule(),
                        "Юнонна", "Ушакова", "Константиновна",
                        "Dentist", "471", "YunonnaUshakova642", "qypzb2XheqYG");
        final DoctorRegistrationDtoResponse actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
        final DoctorRegistrationDtoResponse expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
        Assert.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);

        duration = 15;
        dateStart = dateStart.plusDays(5);
        dateEnd = dateEnd.minusDays(10);
        weekSchedule.clear();
        weekSchedule.put(WeekDay.MONDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(12, 30), LocalTime.of(18, 0)));
        weekSchedule.put(WeekDay.WEDNESDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(11, 15), LocalTime.of(17, 45)));
        weekSchedule.put(WeekDay.THURSDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(8, 45), LocalTime.of(14, 15)));
        weekSchedule.put(WeekDay.FRIDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(10, 30), LocalTime.of(16, 0)));
        generatedWeekSchedule =
                ScheduleGenerators.generateDtoRequestWithDaySchedule(duration, dateStart, dateEnd, weekSchedule);
        final int doctorId = expectedDoctorRegistrationResponse.getId();
        final List<ScheduleCellDtoResponse> expectedSchedule =
                DtoAdapters.transform(ScheduleTransformers.transformWeekDaysSchedule(generatedWeekSchedule, doctorId));

        {
            final EditDoctorScheduleDtoRequest editDoctorScheduleRequest =
                    new EditDoctorScheduleDtoRequest(dateStart.toString(), dateEnd.toString(), duration, generatedWeekSchedule.getWeekDaysSchedule());
            final EditDoctorScheduleDtoResponse expectedResponse =
                    new EditDoctorScheduleDtoResponse(doctorId,
                            expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(),
                            expectedDoctorRegistrationResponse.getPatronymic(), expectedDoctorRegistrationResponse.getSpeciality(),
                            expectedDoctorRegistrationResponse.getRoom(), expectedSchedule);
            final EditDoctorScheduleDtoResponse actualResponse = editDoctorSchedule(rootAdminSessionId, doctorId, editDoctorScheduleRequest);

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        logout(rootAdminSessionId);
    }
}