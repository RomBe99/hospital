package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.admin.*;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.schedule.ScheduleCellDtoResponse;
import net.thumbtack.hospital.util.ScheduleGenerators;
import net.thumbtack.hospital.util.WeekDay;
import net.thumbtack.hospital.util.adapter.DtoAdapters;
import net.thumbtack.hospital.util.adapter.ScheduleTransformers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RunWith(SpringRunner.class)
public class AdministratorControllerTest extends ControllerTestApi {
    @Test
    public void administratorRegistrationTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();
        AdminRegistrationDtoRequest registrationRequest =
                new AdminRegistrationDtoRequest("Аласдаир", "Охота", "Test admin №1", "OhotaAlasdair252", "SDQGjC4GUvAB");

        AdminRegistrationDtoResponse expectedResponse = new AdminRegistrationDtoResponse(
                registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(),
                registrationRequest.getPosition());
        administratorRegistration(rootAdminSessionId, registrationRequest, expectedResponse);

        String testAdminSessionId = login(registrationRequest.getLogin(), registrationRequest.getPassword(),
                new AdminLoginDtoResponse(registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(), registrationRequest.getPosition()));

        getUserInformation(testAdminSessionId,
                new AdminInformationDtoResponse(registrationRequest.getLogin(), registrationRequest.getPassword(),
                        registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(),
                        registrationRequest.getPosition()));

        logout(rootAdminSessionId);
        logout(testAdminSessionId);
    }

    @Test
    public void editAdministratorProfileTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();
        AdminRegistrationDtoRequest registrationRequest =
                new AdminRegistrationDtoRequest("Аласдаир", "Охота", "Test admin №1", "OhotaAlasdair252", "SDQGjC4GUvAB");

        AdminRegistrationDtoResponse expectedResponse = new AdminRegistrationDtoResponse(
                registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(),
                registrationRequest.getPosition());
        administratorRegistration(rootAdminSessionId, registrationRequest, expectedResponse);

        AdminLoginDtoResponse adminLoginResponse =
                new AdminLoginDtoResponse(registrationRequest.getFirstName(), registrationRequest.getLastName(),
                        registrationRequest.getPatronymic(), registrationRequest.getPosition());

        String testAdminSessionId = login(registrationRequest.getLogin(), registrationRequest.getPassword(),
                adminLoginResponse);

        EditAdminProfileDtoRequest editAdminProfileRequest =
                new EditAdminProfileDtoRequest("Алимад", "Федоров",
                        registrationRequest.getPosition(), registrationRequest.getPassword(), "OMoqtge7e1V1");

        editAdministratorProfile(testAdminSessionId, editAdminProfileRequest,
                new EditAdminProfileDtoResponse(adminLoginResponse.getId(), editAdminProfileRequest.getFirstName(), editAdminProfileRequest.getLastName(), editAdminProfileRequest.getPatronymic(), editAdminProfileRequest.getPosition()));

        getUserInformation(testAdminSessionId,
                new AdminInformationDtoResponse(registrationRequest.getLogin(), editAdminProfileRequest.getNewPassword(),
                        editAdminProfileRequest.getFirstName(), editAdminProfileRequest.getLastName(), editAdminProfileRequest.getPatronymic(),
                        editAdminProfileRequest.getPosition()));

        logout(rootAdminSessionId);
        logout(testAdminSessionId);
    }

    @Test
    public void removeDoctorTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();

        LocalDate dateStart = LocalDate.now();
        LocalDate dateEnd = dateStart.plusDays(5);

        DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(dateStart.toString(), dateEnd.toString(), 15, Collections.emptyList(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        DoctorRegistrationDtoResponse doctorRegistrationResponse =
                new DoctorRegistrationDtoResponse(doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                        doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), Collections.emptyList());

        doctorRegistration(rootAdminSessionId, doctorRegistrationRequest, doctorRegistrationResponse);

        RemoveDoctorDtoRequest removeDoctorRequest = new RemoveDoctorDtoRequest(LocalDate.now().toString());
        removeDoctor(rootAdminSessionId, doctorRegistrationResponse.getId(), removeDoctorRequest);

        logout(rootAdminSessionId);
    }

    @Test
    public void getPatientInformationByAdminAndDoctorTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();

        PatientRegistrationDtoRequest patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Аббо", "Титов", "vixej10213@inmail92.com",
                        "399822, г. Дербент, ул. Онежская, дом 77, квартира 773", "+7 (922) 069-47-76",
                        "AbboTitov756", "zRAMIyf7uUVc");
        PatientRegistrationDtoResponse patientRegistrationResponse =
                new PatientRegistrationDtoResponse(patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                        patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());
        String patientSessionId = patientRegistration(patientRegistrationRequest, patientRegistrationResponse);

        PatientInformationDtoResponse patientInformationResponse =
                new PatientInformationDtoResponse(patientRegistrationResponse.getId(),
                        patientRegistrationResponse.getFirstName(), patientRegistrationResponse.getLastName(), patientRegistrationResponse.getPatronymic(),
                        patientRegistrationResponse.getEmail(), patientRegistrationResponse.getAddress(), patientRegistrationResponse.getPhone());
        getPatientInformation(rootAdminSessionId, patientRegistrationResponse.getId(), patientInformationResponse);

        DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(LocalDate.now().toString(), LocalDate.now().toString(), 15, Collections.emptyList(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        DoctorRegistrationDtoResponse doctorRegistrationResponse =
                new DoctorRegistrationDtoResponse(doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                        doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), Collections.emptyList());
        doctorRegistration(rootAdminSessionId, doctorRegistrationRequest, doctorRegistrationResponse);

        String doctorSessionId = login(doctorRegistrationRequest.getLogin(), doctorRegistrationRequest.getPassword(),
                new DoctorLoginDtoResponse(doctorRegistrationResponse.getFirstName(), doctorRegistrationResponse.getLastName(), doctorRegistrationResponse.getPatronymic(),
                        doctorRegistrationResponse.getSpeciality(), doctorRegistrationResponse.getRoom(), doctorRegistrationResponse.getSchedule()));
        getPatientInformation(doctorSessionId, patientRegistrationResponse.getId(), patientInformationResponse);

        logout(doctorSessionId);
        logout(patientSessionId);
        logout(rootAdminSessionId);
    }

    @Test
    public void insertDoctorWithWeekScheduleTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();

        int duration = 15;
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = LocalDate.of(2020, 3, 31);
        LocalTime timeStart = LocalTime.of(8, 0);
        LocalTime timeEnd = LocalTime.of(17, 0);
        List<Integer> weekDays = Arrays.asList(1, 2, 3);

        DtoRequestWithSchedule generatedWeekSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(
                duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                        generatedWeekSchedule.getWeekSchedule(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        DoctorRegistrationDtoResponse doctorRegistrationResponse = new DoctorRegistrationDtoResponse(
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), null);
        doctorRegistration(rootAdminSessionId, doctorRegistrationRequest, doctorRegistrationResponse);

        String doctorSessionId = login(doctorRegistrationRequest.getLogin(), doctorRegistrationRequest.getPassword(),
                new DoctorLoginDtoResponse(doctorRegistrationResponse.getFirstName(), doctorRegistrationResponse.getLastName(), doctorRegistrationResponse.getPatronymic(),
                        doctorRegistrationResponse.getSpeciality(), doctorRegistrationResponse.getRoom(), doctorRegistrationResponse.getSchedule()));

        logout(doctorSessionId);
        logout(rootAdminSessionId);
    }

    @Test
    public void insertDoctorWithWeekDayScheduleTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();

        int duration = 30;
        LocalDate dateStart = LocalDate.of(2020, 7, 1);
        LocalDate dateEnd = LocalDate.of(2020, 8, 15);
        Map<WeekDay, AbstractMap.SimpleEntry<LocalTime, LocalTime>> weekSchedule = new HashMap<>();
        weekSchedule.put(WeekDay.MONDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(8, 0), LocalTime.of(14, 0)));
        weekSchedule.put(WeekDay.TUESDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(12, 0), LocalTime.of(18, 0)));
        weekSchedule.put(WeekDay.THURSDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(10, 0), LocalTime.of(16, 0)));

        DtoRequestWithSchedule generatedWeekSchedule =
                ScheduleGenerators.generateDtoRequestWithDaySchedule(duration, dateStart, dateEnd, weekSchedule);

        DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                        generatedWeekSchedule.getWeekDaysSchedule(),
                        "Юнонна", "Ушакова", "Константиновна",
                        "Dentist", "471", "YunonnaUshakova642", "qypzb2XheqYG");
        DoctorRegistrationDtoResponse doctorRegistrationResponse = new DoctorRegistrationDtoResponse(
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), null);
        doctorRegistration(rootAdminSessionId, doctorRegistrationRequest, doctorRegistrationResponse);

        String doctorSessionId = login(doctorRegistrationRequest.getLogin(), doctorRegistrationRequest.getPassword(),
                new DoctorLoginDtoResponse(doctorRegistrationResponse.getFirstName(), doctorRegistrationResponse.getLastName(), doctorRegistrationResponse.getPatronymic(),
                        doctorRegistrationResponse.getSpeciality(), doctorRegistrationResponse.getRoom(), doctorRegistrationResponse.getSchedule()));

        logout(rootAdminSessionId);
        logout(doctorSessionId);
    }

    @Test
    public void editDoctorScheduleWithWeekScheduleTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();

        int duration = 15;
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = LocalDate.of(2020, 3, 31);
        LocalTime timeStart = LocalTime.of(8, 0);
        LocalTime timeEnd = LocalTime.of(17, 0);
        List<Integer> weekDays = Arrays.asList(1, 2, 3);

        DtoRequestWithSchedule generatedWeekSchedule =
                ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                        generatedWeekSchedule.getWeekSchedule(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        DoctorRegistrationDtoResponse doctorRegistrationResponse = new DoctorRegistrationDtoResponse(
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), null);
        doctorRegistration(rootAdminSessionId, doctorRegistrationRequest, doctorRegistrationResponse);

        duration = 30;
        dateEnd = dateStart.plusDays(15);
        timeStart = LocalTime.of(10, 0);
        timeEnd = LocalTime.of(16, 30);
        weekDays = Arrays.asList(1, 3, 4, 5);
        generatedWeekSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(
                duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);
        int doctorId = doctorRegistrationResponse.getId();
        List<ScheduleCellDtoResponse> expectedSchedule =
                DtoAdapters.transform(ScheduleTransformers.transformWeekSchedule(generatedWeekSchedule, doctorId));

        EditDoctorScheduleDtoRequest editDoctorScheduleRequest =
                new EditDoctorScheduleDtoRequest(dateStart.toString(), dateEnd.toString(), duration, generatedWeekSchedule.getWeekSchedule());
        EditDoctorScheduleDtoResponse editDoctorScheduleResponse =
                new EditDoctorScheduleDtoResponse(doctorId,
                        doctorRegistrationResponse.getFirstName(), doctorRegistrationResponse.getLastName(),
                        doctorRegistrationResponse.getPatronymic(), doctorRegistrationResponse.getSpeciality(), doctorRegistrationResponse.getRoom(), expectedSchedule);
        editDoctorSchedule(rootAdminSessionId, doctorId, editDoctorScheduleRequest, editDoctorScheduleResponse);

        logout(rootAdminSessionId);
    }

    @Test
    public void editDoctorScheduleWithWeekDayScheduleTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();

        int duration = 30;
        LocalDate dateStart = LocalDate.of(2020, 7, 1);
        LocalDate dateEnd = LocalDate.of(2020, 8, 15);
        Map<WeekDay, AbstractMap.SimpleEntry<LocalTime, LocalTime>> weekSchedule = new HashMap<>();
        weekSchedule.put(WeekDay.MONDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(8, 0), LocalTime.of(14, 0)));
        weekSchedule.put(WeekDay.TUESDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(12, 0), LocalTime.of(18, 0)));
        weekSchedule.put(WeekDay.FRIDAY, new AbstractMap.SimpleEntry<>(LocalTime.of(10, 0), LocalTime.of(16, 0)));

        DtoRequestWithSchedule generatedWeekSchedule =
                ScheduleGenerators.generateDtoRequestWithDaySchedule(duration, dateStart, dateEnd, weekSchedule);

        DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                        generatedWeekSchedule.getWeekDaysSchedule(),
                        "Юнонна", "Ушакова", "Константиновна",
                        "Dentist", "471", "YunonnaUshakova642", "qypzb2XheqYG");
        DoctorRegistrationDtoResponse doctorRegistrationResponse = new DoctorRegistrationDtoResponse(
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), null);
        doctorRegistration(rootAdminSessionId, doctorRegistrationRequest, doctorRegistrationResponse);

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
        int doctorId = doctorRegistrationResponse.getId();
        List<ScheduleCellDtoResponse> expectedSchedule =
                DtoAdapters.transform(ScheduleTransformers.transformWeekDaysSchedule(generatedWeekSchedule, doctorId));

        EditDoctorScheduleDtoRequest editDoctorScheduleRequest =
                new EditDoctorScheduleDtoRequest(dateStart.toString(), dateEnd.toString(), duration, generatedWeekSchedule.getWeekDaysSchedule());
        EditDoctorScheduleDtoResponse editDoctorScheduleResponse =
                new EditDoctorScheduleDtoResponse(doctorId,
                        doctorRegistrationResponse.getFirstName(), doctorRegistrationResponse.getLastName(),
                        doctorRegistrationResponse.getPatronymic(), doctorRegistrationResponse.getSpeciality(), doctorRegistrationResponse.getRoom(), expectedSchedule);
        editDoctorSchedule(rootAdminSessionId, doctorId, editDoctorScheduleRequest, editDoctorScheduleResponse);

        logout(rootAdminSessionId);
    }
}