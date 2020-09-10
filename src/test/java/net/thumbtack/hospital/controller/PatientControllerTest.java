package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ErrorDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ErrorsDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.AppointmentToDoctorDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.EditPatientProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.FullPatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.util.ScheduleGenerators;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.ScheduleErrorCode;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
public class PatientControllerTest extends ControllerTestApi {
    @Test
    public void patientRegistrationTest() throws Exception {
        PatientRegistrationDtoRequest patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Пахон", "Петров",
                        "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                        "PahonPetrov927", "ugSfPaGD1YBv");

        String patientSessionId = patientRegistration(patientRegistrationRequest,
                new PatientRegistrationDtoResponse(patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                        patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone()));

        getUserInformation(patientSessionId,
                new FullPatientInformationDtoResponse(patientRegistrationRequest.getLogin(), patientRegistrationRequest.getPassword(),
                        patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                        patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone()));

        logout(patientSessionId);
    }

    @Test
    public void editPatientProfileTest() throws Exception {
        PatientRegistrationDtoRequest patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Пахон", "Петров",
                        "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                        "PahonPetrov927", "ugSfPaGD1YBv");

        String patientSessionId = patientRegistration(patientRegistrationRequest,
                new PatientRegistrationDtoResponse(patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                        patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone()));

        getUserInformation(patientSessionId,
                new FullPatientInformationDtoResponse(patientRegistrationRequest.getLogin(), patientRegistrationRequest.getPassword(),
                        patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                        patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone()));

        EditPatientProfileDtoRequest editPatientProfileRequest =
                new EditPatientProfileDtoRequest("Арифе", "Репин", "Вадимович",
                        "camikaf920@mijumail.com", "433832, г. Карагай, ул. Сходненский проезд, дом 18, квартира 592", "8922) 790-31-77",
                        patientRegistrationRequest.getPassword(), patientRegistrationRequest.getPassword());

        editPatientProfile(patientSessionId, editPatientProfileRequest,
                new EditPatientProfileDtoResponse(editPatientProfileRequest.getFirstName(), editPatientProfileRequest.getLastName(), editPatientProfileRequest.getPatronymic(),
                        editPatientProfileRequest.getEmail(), editPatientProfileRequest.getAddress(), editPatientProfileRequest.getPhone(),
                        editPatientProfileRequest.getNewPassword()));

        getUserInformation(patientSessionId,
                new FullPatientInformationDtoResponse(patientRegistrationRequest.getLogin(), editPatientProfileRequest.getNewPassword(),
                        editPatientProfileRequest.getFirstName(), editPatientProfileRequest.getLastName(), editPatientProfileRequest.getPatronymic(),
                        editPatientProfileRequest.getEmail(), editPatientProfileRequest.getAddress(), editPatientProfileRequest.getPhone()));

        logout(patientSessionId);
    }

    @Test
    public void appointmentToDoctorTest() throws Exception {
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

        logout(rootAdminSessionId);

        PatientRegistrationDtoRequest patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Пахон", "Петров",
                        "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                        "PahonPetrov927", "ugSfPaGD1YBv");

        String patientSessionId = patientRegistration(patientRegistrationRequest,
                new PatientRegistrationDtoResponse(patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                        patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone()));

        LocalDate appointmentDate = LocalDate.of(2020, 3, 17);
        LocalTime appointmentTime = LocalTime.of(10, 15);
        AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                doctorRegistrationResponse.getId(), doctorRegistrationResponse.getSpeciality(),
                appointmentDate.toString(), appointmentTime.toString());

        String expectedTicket = TicketFactory.buildTicketToDoctor(appointmentToDoctorRequest.getDoctorId(), appointmentDate, appointmentTime);
        AppointmentToDoctorDtoResponse appointmentToDoctorResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, appointmentToDoctorRequest.getDoctorId(),
                doctorRegistrationResponse.getFirstName(), doctorRegistrationResponse.getLastName(), doctorRegistrationResponse.getPatronymic(),
                doctorRegistrationResponse.getSpeciality(), doctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());

        appointmentToDoctor(patientSessionId, appointmentToDoctorRequest, appointmentToDoctorResponse);
    }

    @Test
    public void denyAppointmentToDoctorTest() throws Exception {
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

        logout(rootAdminSessionId);

        PatientRegistrationDtoRequest patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Пахон", "Петров",
                        "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                        "PahonPetrov927", "ugSfPaGD1YBv");

        String patientSessionId = patientRegistration(patientRegistrationRequest,
                new PatientRegistrationDtoResponse(patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                        patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone()));

        LocalDate appointmentDate = LocalDate.of(2020, 3, 17);
        LocalTime appointmentTime = LocalTime.of(10, 15);
        AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                doctorRegistrationResponse.getId(), doctorRegistrationResponse.getSpeciality(),
                appointmentDate.toString(), appointmentTime.toString());

        String expectedTicket = TicketFactory.buildTicketToDoctor(appointmentToDoctorRequest.getDoctorId(), appointmentDate, appointmentTime);
        AppointmentToDoctorDtoResponse appointmentToDoctorResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, appointmentToDoctorRequest.getDoctorId(),
                doctorRegistrationResponse.getFirstName(), doctorRegistrationResponse.getLastName(), doctorRegistrationResponse.getPatronymic(),
                doctorRegistrationResponse.getSpeciality(), doctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());

        appointmentToDoctor(patientSessionId, appointmentToDoctorRequest, appointmentToDoctorResponse);
        denyTicketToDoctor(patientSessionId, expectedTicket);
    }

    @Test
    public void appointmentToDoctorTwoTimesTest() throws Exception {
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

        logout(rootAdminSessionId);

        PatientRegistrationDtoRequest patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Пахон", "Петров",
                        "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                        "PahonPetrov927", "ugSfPaGD1YBv");

        String patientSessionId = patientRegistration(patientRegistrationRequest,
                new PatientRegistrationDtoResponse(patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                        patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone()));

        LocalDate appointmentDate = LocalDate.of(2020, 3, 17);
        LocalTime appointmentTime = LocalTime.of(10, 15);
        AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                doctorRegistrationResponse.getId(), doctorRegistrationResponse.getSpeciality(),
                appointmentDate.toString(), appointmentTime.toString());

        String expectedTicket = TicketFactory.buildTicketToDoctor(appointmentToDoctorRequest.getDoctorId(), appointmentDate, appointmentTime);
        AppointmentToDoctorDtoResponse appointmentToDoctorResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, appointmentToDoctorRequest.getDoctorId(),
                doctorRegistrationResponse.getFirstName(), doctorRegistrationResponse.getLastName(), doctorRegistrationResponse.getPatronymic(),
                doctorRegistrationResponse.getSpeciality(), doctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());

        appointmentToDoctor(patientSessionId, appointmentToDoctorRequest, appointmentToDoctorResponse);

        String url = PatientController.PREFIX_URL + "/" + PatientController.APPOINTMENT_TO_DOCTOR_URL;
        String json = mapToJson(appointmentToDoctorRequest);

        String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, patientSessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        ErrorsDtoResponse actualErrorsDtoResponse = mapFromJson(actualJsonResponse, ErrorsDtoResponse.class);
        ErrorsDtoResponse expectedErrorsDtoResponse = new ErrorsDtoResponse(Collections.singletonList(
                new ErrorDtoResponse(ScheduleErrorCode.ALREADY_CONTAINS_APPOINTMENT.getErrorCode(), "date and time", ScheduleErrorCode.ALREADY_CONTAINS_APPOINTMENT.getErrorMessage())));
        Assert.assertEquals(expectedErrorsDtoResponse, actualErrorsDtoResponse);
    }
}