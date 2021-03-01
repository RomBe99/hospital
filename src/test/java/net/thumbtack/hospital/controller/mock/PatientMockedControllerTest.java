package net.thumbtack.hospital.controller.mock;

import net.thumbtack.hospital.controller.PatientController;
import net.thumbtack.hospital.controller.api.MockedControllerTestApi;
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
import net.thumbtack.hospital.util.error.DoctorNotFoundErrorCode;
import net.thumbtack.hospital.util.error.ScheduleErrorCode;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.*;

@RunWith(SpringRunner.class)
public class PatientMockedControllerTest extends MockedControllerTestApi {
    @Test
    public void patientRegistrationTest() throws Exception {
        final PatientRegistrationDtoRequest patientRegistrationRequest = new PatientRegistrationDtoRequest(
                "Пахон", "Петров",
                "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                "PahonPetrov927", "ugSfPaGD1YBv");

        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);

        {
            final PatientRegistrationDtoResponse actualResponse = patientRegistrationData.getValue();
            final PatientRegistrationDtoResponse expectedResponse = new PatientRegistrationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final String patientSessionId = patientRegistrationData.getKey();

        {
            final FullPatientInformationDtoResponse actualResponse = getUserInformation(patientSessionId, FullPatientInformationDtoResponse.class);
            final FullPatientInformationDtoResponse expectedResponse = new FullPatientInformationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getLogin(), patientRegistrationRequest.getPassword(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        logout(patientSessionId);
    }

    @Test
    public void editPatientProfileTest() throws Exception {
        final PatientRegistrationDtoRequest patientRegistrationRequest = new PatientRegistrationDtoRequest(
                "Пахон", "Петров",
                "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                "PahonPetrov927", "ugSfPaGD1YBv");

        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);

        {
            final PatientRegistrationDtoResponse actualResponse = patientRegistrationData.getValue();
            final PatientRegistrationDtoResponse expectedResponse = new PatientRegistrationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final String patientSessionId = patientRegistrationData.getKey();

        {
            final FullPatientInformationDtoResponse actualResponse = getUserInformation(patientSessionId, FullPatientInformationDtoResponse.class);
            final FullPatientInformationDtoResponse expectedResponse = new FullPatientInformationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getLogin(), patientRegistrationRequest.getPassword(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }


        final EditPatientProfileDtoRequest editPatientProfileRequest = new EditPatientProfileDtoRequest(
                "Арифе", "Репин", "Вадимович",
                "camikaf920@mijumail.com", "433832, г. Карагай, ул. Сходненский проезд, дом 18, квартира 592", "8922) 790-31-77",
                patientRegistrationRequest.getPassword(), null);

        {
            final EditPatientProfileDtoResponse actualResponse = editPatientProfile(patientSessionId, editPatientProfileRequest);
            final EditPatientProfileDtoResponse expectedResponse = new EditPatientProfileDtoResponse(
                    editPatientProfileRequest.getFirstName(), editPatientProfileRequest.getLastName(), editPatientProfileRequest.getPatronymic(),
                    editPatientProfileRequest.getEmail(), editPatientProfileRequest.getAddress(), actualResponse.getPhone(),
                    editPatientProfileRequest.getOldPassword());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        {
            final FullPatientInformationDtoResponse actualResponse = getUserInformation(patientSessionId, FullPatientInformationDtoResponse.class);
            final FullPatientInformationDtoResponse expectedResponse = new FullPatientInformationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getLogin(), editPatientProfileRequest.getOldPassword(),
                    editPatientProfileRequest.getFirstName(), editPatientProfileRequest.getLastName(), editPatientProfileRequest.getPatronymic(),
                    editPatientProfileRequest.getEmail(), editPatientProfileRequest.getAddress(), editPatientProfileRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }


        logout(patientSessionId);
    }

    @Test
    public void appointmentToDoctorTest() throws Exception {
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

        logout(rootAdminSessionId);

        final PatientRegistrationDtoRequest patientRegistrationRequest = new PatientRegistrationDtoRequest(
                "Пахон", "Петров",
                "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                "PahonPetrov927", "ugSfPaGD1YBv");

        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);

        {
            final PatientRegistrationDtoResponse actualResponse = patientRegistrationData.getValue();
            final PatientRegistrationDtoResponse expectedResponse = new PatientRegistrationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final String patientSessionId = patientRegistrationData.getKey();

        final LocalDate appointmentDate = LocalDate.of(2020, 3, 17);
        final LocalTime appointmentTime = LocalTime.of(10, 15);
        final AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                expectedDoctorRegistrationResponse.getId(), appointmentDate.toString(), appointmentTime.toString());

        final String expectedTicket = TicketFactory.buildTicketToDoctor(appointmentToDoctorRequest.getDoctorId(), appointmentDate, appointmentTime);
        final AppointmentToDoctorDtoResponse expectedResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, appointmentToDoctorRequest.getDoctorId(),
                expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());
        final AppointmentToDoctorDtoResponse actualResponse = appointmentToDoctor(patientSessionId, appointmentToDoctorRequest);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void denyAppointmentToDoctorTest() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        int duration = 15;
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = LocalDate.of(2020, 3, 31);
        LocalTime timeStart = LocalTime.of(8, 0);
        LocalTime timeEnd = LocalTime.of(17, 0);
        List<Integer> weekDays = Arrays.asList(1, 2, 3);

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

        logout(rootAdminSessionId);

        final PatientRegistrationDtoRequest patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Пахон", "Петров",
                        "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                        "PahonPetrov927", "ugSfPaGD1YBv");

        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);

        {
            final PatientRegistrationDtoResponse actualResponse = patientRegistrationData.getValue();
            final PatientRegistrationDtoResponse expectedResponse = new PatientRegistrationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final String patientSessionId = patientRegistrationData.getKey();

        final LocalDate appointmentDate = LocalDate.of(2020, 3, 17);
        final LocalTime appointmentTime = LocalTime.of(10, 15);
        final AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                expectedDoctorRegistrationResponse.getId(), appointmentDate.toString(), appointmentTime.toString());

        final String expectedTicket = TicketFactory.buildTicketToDoctor(appointmentToDoctorRequest.getDoctorId(), appointmentDate, appointmentTime);
        final AppointmentToDoctorDtoResponse expectedResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, appointmentToDoctorRequest.getDoctorId(),
                expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());

        final AppointmentToDoctorDtoResponse actualResponse = appointmentToDoctor(patientSessionId, appointmentToDoctorRequest);
        Assert.assertEquals(expectedResponse, actualResponse);

        denyTicketToDoctor(patientSessionId, expectedTicket);
    }

    @Test
    public void appointmentToDoctorTwoTimesTest() throws Exception {
        String rootAdminSessionId = loginRootAdmin();

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

        logout(rootAdminSessionId);

        final PatientRegistrationDtoRequest patientRegistrationRequest = new PatientRegistrationDtoRequest(
                "Пахон", "Петров",
                "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                "PahonPetrov927", "ugSfPaGD1YBv");

        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);

        {
            final PatientRegistrationDtoResponse actualResponse = patientRegistrationData.getValue();
            final PatientRegistrationDtoResponse expectedResponse = new PatientRegistrationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final String patientSessionId = patientRegistrationData.getKey();

        final LocalDate appointmentDate = LocalDate.of(2020, 3, 17);
        final LocalTime appointmentTime = LocalTime.of(10, 15);
        final AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                expectedDoctorRegistrationResponse.getId(), appointmentDate.toString(), appointmentTime.toString());

        final String expectedTicket = TicketFactory.buildTicketToDoctor(appointmentToDoctorRequest.getDoctorId(), appointmentDate, appointmentTime);
        final AppointmentToDoctorDtoResponse expectedResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, appointmentToDoctorRequest.getDoctorId(),
                expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());

        final AppointmentToDoctorDtoResponse actualResponse = appointmentToDoctor(patientSessionId, appointmentToDoctorRequest);
        Assert.assertEquals(expectedResponse, actualResponse);

        final String url = buildUrl(PatientController.PREFIX_URL, PatientController.APPOINTMENT_TO_DOCTOR_URL);
        final String json = mapToJson(appointmentToDoctorRequest);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, patientSessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final ErrorsDtoResponse actualErrorsDtoResponse = mapFromJson(actualJsonResponse, ErrorsDtoResponse.class);
        final ErrorsDtoResponse expectedErrorsDtoResponse = new ErrorsDtoResponse(Collections.singletonList(
                new ErrorDtoResponse(ScheduleErrorCode.ALREADY_CONTAINS_APPOINTMENT.getErrorCode(), "date and time", ScheduleErrorCode.ALREADY_CONTAINS_APPOINTMENT.getErrorMessage())));
        Assert.assertEquals(expectedErrorsDtoResponse, actualErrorsDtoResponse);
    }

    @Test
    public void appointmentToDoctorBySpecialityTest1() throws Exception {
        final PatientRegistrationDtoRequest patientRegistrationRequest = new PatientRegistrationDtoRequest(
                "Пахон", "Петров",
                "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                "PahonPetrov927", "ugSfPaGD1YBv");

        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);

        {
            final PatientRegistrationDtoResponse actualResponse = patientRegistrationData.getValue();
            final PatientRegistrationDtoResponse expectedResponse = new PatientRegistrationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final String patientSessionId = patientRegistrationData.getKey();

        final LocalDate appointmentDate = LocalDate.of(2020, 3, 17);
        final LocalTime appointmentTime = LocalTime.of(10, 15);
        final AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                "Dentist", appointmentDate.toString(), appointmentTime.toString());

        final String url = buildUrl(PatientController.PREFIX_URL, PatientController.APPOINTMENT_TO_DOCTOR_URL);
        final String json = mapToJson(appointmentToDoctorRequest);

        final String actualJsonResponse = mvc.perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, patientSessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assert.assertFalse(actualJsonResponse.isEmpty());

        final ErrorsDtoResponse actualErrorsDtoResponse = mapFromJson(actualJsonResponse, ErrorsDtoResponse.class);
        final ErrorsDtoResponse expectedErrorsDtoResponse = new ErrorsDtoResponse(Collections.singletonList(
                new ErrorDtoResponse(DoctorNotFoundErrorCode.DOCTOR_NOT_FOUND.getErrorCode(), "id or speciality", DoctorNotFoundErrorCode.DOCTOR_NOT_FOUND.getErrorMessage())));
        Assert.assertEquals(expectedErrorsDtoResponse, actualErrorsDtoResponse);
    }

    @Test
    public void appointmentToDoctorBySpecialityTest2() throws Exception {
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

        logout(rootAdminSessionId);

        final PatientRegistrationDtoRequest patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Пахон", "Петров",
                        "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                        "PahonPetrov927", "ugSfPaGD1YBv");

        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);

        {
            final PatientRegistrationDtoResponse actualResponse = patientRegistrationData.getValue();
            final PatientRegistrationDtoResponse expectedResponse = new PatientRegistrationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final String patientSessionId = patientRegistrationData.getKey();

        final LocalDate appointmentDate = LocalDate.of(2020, 3, 17);
        final LocalTime appointmentTime = LocalTime.of(10, 15);
        final AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                expectedDoctorRegistrationResponse.getSpeciality(), appointmentDate.toString(), appointmentTime.toString());

        final String expectedTicket = TicketFactory.buildTicketToDoctor(expectedDoctorRegistrationResponse.getId(), appointmentDate, appointmentTime);
        final AppointmentToDoctorDtoResponse expectedResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, expectedDoctorRegistrationResponse.getId(),
                expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());
        final AppointmentToDoctorDtoResponse actualResponse = appointmentToDoctor(patientSessionId, appointmentToDoctorRequest);

        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void appointmentToDoctorBySpecialityTest3() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        final int duration = 15;
        final LocalDate dateStart = LocalDate.of(2020, 3, 1);
        final LocalDate dateEnd = LocalDate.of(2020, 3, 31);
        final LocalTime timeStart = LocalTime.of(8, 0);
        final LocalTime timeEnd = LocalTime.of(17, 0);
        final List<Integer> weekDays = Arrays.asList(1, 2, 3);
        final String speciality = "Surgeon";
        final Map<Integer, DoctorRegistrationDtoResponse> responses = new HashMap<>();

        final DtoRequestWithSchedule generatedWeekSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(
                duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        {
            final DoctorRegistrationDtoRequest doctorRegistrationRequest = new DoctorRegistrationDtoRequest(
                    generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                    generatedWeekSchedule.getWeekSchedule(),
                    "Саркис", "Семёнов", "Вениаминович",
                    speciality, "205", "SarkisSemenov585", "xjNE6QK6d3b9");
            final DoctorRegistrationDtoResponse actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
            final DoctorRegistrationDtoResponse expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                    doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                    doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
            Assert.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);
            responses.put(expectedDoctorRegistrationResponse.getId(), expectedDoctorRegistrationResponse);
        }

        {
            final DoctorRegistrationDtoRequest doctorRegistrationRequest = new DoctorRegistrationDtoRequest(
                    generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                    generatedWeekSchedule.getWeekDaysSchedule(),
                    "Аксырга", "Прокофьева", "Леонидовна",
                    speciality, "306", "AksyrgaProkofeva993", "TiW0wvq2HNaB");
            final DoctorRegistrationDtoResponse actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
            final DoctorRegistrationDtoResponse expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                    doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                    doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
            Assert.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);
            responses.put(expectedDoctorRegistrationResponse.getId(), expectedDoctorRegistrationResponse);
        }

        {
            final DoctorRegistrationDtoRequest doctorRegistrationRequest = new DoctorRegistrationDtoRequest(
                    generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                    generatedWeekSchedule.getWeekDaysSchedule(),
                    "Чилина", "Царева", "Ермаковна",
                    speciality, "261", "ChilinaTsareva849", "9LZfrHND9Nrq");
            final DoctorRegistrationDtoResponse actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
            final DoctorRegistrationDtoResponse expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                    doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                    doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
            Assert.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);
            responses.put(expectedDoctorRegistrationResponse.getId(), expectedDoctorRegistrationResponse);
        }

        logout(rootAdminSessionId);


        final PatientRegistrationDtoRequest patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Пахон", "Петров",
                        "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                        "PahonPetrov927", "ugSfPaGD1YBv");

        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);

        {
            final PatientRegistrationDtoResponse actualResponse = patientRegistrationData.getValue();
            final PatientRegistrationDtoResponse expectedResponse = new PatientRegistrationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final String patientSessionId = patientRegistrationData.getKey();

        final LocalDate appointmentDate = LocalDate.of(2020, 3, 17);
        final LocalTime appointmentTime = LocalTime.of(10, 15);
        final AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(speciality, appointmentDate.toString(), appointmentTime.toString());

        final AppointmentToDoctorDtoResponse actualResponse = appointmentToDoctor(patientSessionId, appointmentToDoctorRequest);
        final DoctorRegistrationDtoResponse docResponse = responses.get(actualResponse.getDoctorId());

        final String expectedTicket = TicketFactory.buildTicketToDoctor(docResponse.getId(), appointmentDate, appointmentTime);
        final AppointmentToDoctorDtoResponse expectedResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, docResponse.getId(),
                docResponse.getFirstName(), docResponse.getLastName(), docResponse.getPatronymic(),
                docResponse.getSpeciality(), docResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString()
        );

        Assert.assertEquals(expectedResponse, actualResponse);
    }
}