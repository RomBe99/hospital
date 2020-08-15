package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.AdminRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.admin.EditAdminProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.admin.RemoveDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.admin.*;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.util.ScheduleGenerators;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
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

        getUserInformation(testAdminSessionId, UserType.ADMINISTRATOR,
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

        getUserInformation(testAdminSessionId, UserType.ADMINISTRATOR,
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
                new DoctorRegistrationDtoRequest(LocalDate.now().toString(), LocalDate.now().toString(), 15, new ArrayList<>(),
                        "Саркис", "Семёнов", "Вениаминович",
                        "Surgeon", "205", "SarkisSemenov585", "xjNE6QK6d3b9");
        DoctorRegistrationDtoResponse doctorRegistrationResponse =
                new DoctorRegistrationDtoResponse(doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                        doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), new ArrayList<>());
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
                new DoctorRegistrationDtoRequest(generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), duration,
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
}