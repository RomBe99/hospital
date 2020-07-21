package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.*;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.admin.*;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.mapper.UserTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        getUserInformation(testAdminSessionId, UserTypes.ADMINISTRATOR,
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

        getUserInformation(testAdminSessionId, UserTypes.ADMINISTRATOR,
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
                new DoctorRegistrationDtoRequest(dateStart.toString(), dateEnd.toString(), 15, new ArrayList<>(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        DoctorRegistrationDtoResponse doctorRegistrationResponse =
                new DoctorRegistrationDtoResponse(doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                        doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), new ArrayList<>());

        doctorRegistration(rootAdminSessionId, doctorRegistrationRequest, doctorRegistrationResponse);

        RemoveDoctorDtoRequest removeDoctorRequest = new RemoveDoctorDtoRequest(LocalDate.now().toString());
        removeDoctor(rootAdminSessionId, doctorRegistrationResponse.getId(), removeDoctorRequest);

        logout(rootAdminSessionId);
    }

    @Test
    public void insertDoctorWithWeekScheduleTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();

        int duration = 15;
        int durationPerDay = 4;
        int daysCount = 7;
        List<Integer> weekDays = Arrays.asList(1, 2, 3);

        DtoRequestWithSchedule generatedSchedule = generateWeekSchedule(duration, durationPerDay, daysCount, weekDays);

        DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest(generatedSchedule.getDateStart(), generatedSchedule.getDateEnd(), duration,
                        generatedSchedule.getWeekSchedule(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        DoctorRegistrationDtoResponse expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(
                doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), null);
        doctorRegistration(rootAdminSessionId, doctorRegistrationRequest, expectedDoctorRegistrationResponse);

        String doctorSessionId = login(doctorRegistrationRequest.getLogin(), doctorRegistrationRequest.getPassword(),
                new DoctorLoginDtoResponse(expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                        expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), expectedDoctorRegistrationResponse.getSchedule()));

        logout(doctorSessionId);
        logout(rootAdminSessionId);
    }
}