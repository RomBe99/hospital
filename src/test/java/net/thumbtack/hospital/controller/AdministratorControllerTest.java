package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.AdminRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.admin.EditAdminProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.admin.RemoveDoctorDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.AdminLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.AdminRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.admin.EditAdminProfileDtoResponse;
import net.thumbtack.hospital.mapper.UserTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
public class AdministratorControllerTest extends BaseControllerTest {
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
                new AdminLoginDtoResponse(registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPatronymic(), registrationRequest.getPosition()),
                UserTypes.ADMINISTRATOR);

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
                adminLoginResponse,
                UserTypes.ADMINISTRATOR);

        EditAdminProfileDtoRequest editAdminProfileRequest =
                new EditAdminProfileDtoRequest("Алимад", "Федоров",
                        registrationRequest.getPosition(), registrationRequest.getPassword(), "OMoqtge7e1V1");

        editAdministratorProfile(testAdminSessionId, editAdminProfileRequest,
                new EditAdminProfileDtoResponse(adminLoginResponse.getId(), editAdminProfileRequest.getFirstName(), editAdminProfileRequest.getLastName(), editAdminProfileRequest.getPatronymic(), editAdminProfileRequest.getPosition()));

        logout(rootAdminSessionId);
        logout(testAdminSessionId);
    }

    @Test
    public void removeDoctorTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();
        DoctorRegistrationDtoRequest doctorRegistrationRequest =
                new DoctorRegistrationDtoRequest("Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9",
                        LocalDate.now().toString(), LocalDate.now().plusDays(5).toString(), new ArrayList<>(), null, 15);
        DoctorRegistrationDtoResponse doctorRegistrationResponse = new DoctorRegistrationDtoResponse(doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), new ArrayList<>());

        doctorRegistration(rootAdminSessionId, doctorRegistrationRequest, doctorRegistrationResponse);

        RemoveDoctorDtoRequest removeDoctorRequest = new RemoveDoctorDtoRequest(LocalDate.now().toString());
        removeDoctor(rootAdminSessionId, doctorRegistrationResponse.getId(), removeDoctorRequest);

        logout(rootAdminSessionId);
    }
}