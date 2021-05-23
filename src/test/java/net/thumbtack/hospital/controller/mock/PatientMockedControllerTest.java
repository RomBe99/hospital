package net.thumbtack.hospital.controller.mock;

import net.thumbtack.hospital.controller.PatientController;
import net.thumbtack.hospital.controller.api.MockedControllerTestApi;
import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ErrorDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ErrorsDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.AppointmentToDoctorDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.EditPatientProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.FullPatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.DoctorNotFoundErrorCode;
import net.thumbtack.hospital.util.error.ScheduleErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class PatientMockedControllerTest extends MockedControllerTestApi {
    @Test
    public void patientRegistrationTest() throws Exception {
        final PatientRegistrationDtoRequest patientRegistrationRequest = new PatientRegistrationDtoRequest(
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

        final var patientSessionId = patientRegistrationData.getKey();

        {
            final var actualResponse = getUserInformation(patientSessionId, FullPatientInformationDtoResponse.class);
            final var expectedResponse = new FullPatientInformationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getLogin(), patientRegistrationRequest.getPassword(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        logout(patientSessionId);
    }

    @Test
    public void editPatientProfileTest() throws Exception {
        final var patientRegistrationRequest = new PatientRegistrationDtoRequest("Пахон", "Петров",
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

        final var patientSessionId = patientRegistrationData.getKey();

        {
            final var actualResponse = getUserInformation(patientSessionId, FullPatientInformationDtoResponse.class);
            final var expectedResponse = new FullPatientInformationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getLogin(), patientRegistrationRequest.getPassword(),
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }


        final var editPatientProfileRequest = new EditPatientProfileDtoRequest(
                "Арифе", "Репин", "Вадимович",
                "camikaf920@mijumail.com", "433832, г. Карагай, ул. Сходненский проезд, дом 18, квартира 592", "8922) 790-31-77",
                patientRegistrationRequest.getPassword(), null);

        {
            final var actualResponse = editPatientProfile(patientSessionId, editPatientProfileRequest);
            final var expectedResponse = new EditPatientProfileDtoResponse(
                    editPatientProfileRequest.getFirstName(), editPatientProfileRequest.getLastName(), editPatientProfileRequest.getPatronymic(),
                    editPatientProfileRequest.getEmail(), editPatientProfileRequest.getAddress(), actualResponse.getPhone(),
                    editPatientProfileRequest.getOldPassword());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        {
            final var actualResponse = getUserInformation(patientSessionId, FullPatientInformationDtoResponse.class);
            final var expectedResponse = new FullPatientInformationDtoResponse(actualResponse.getId(),
                    patientRegistrationRequest.getLogin(), editPatientProfileRequest.getOldPassword(),
                    editPatientProfileRequest.getFirstName(), editPatientProfileRequest.getLastName(), editPatientProfileRequest.getPatronymic(),
                    editPatientProfileRequest.getEmail(), editPatientProfileRequest.getAddress(), editPatientProfileRequest.getPhone());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }


        logout(patientSessionId);
    }

    @Test
    public void appointmentToDoctorTest() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        final var duration = 15;
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = LocalDate.of(2020, 3, 31);
        final var timeStart = LocalTime.of(8, 0);
        final var timeEnd = LocalTime.of(17, 0);
        final var weekDays = List.of(1, 2, 3);

        final var generatedWeekSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(
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

        logout(rootAdminSessionId);

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

        final var patientSessionId = patientRegistrationData.getKey();

        final var appointmentDate = LocalDate.of(2020, 3, 17);
        final var appointmentTime = LocalTime.of(10, 15);
        final var appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                expectedDoctorRegistrationResponse.getId(), appointmentDate.toString(), appointmentTime.toString());

        final var expectedTicket = getTicketFactory().buildTicketToDoctor(appointmentToDoctorRequest.getDoctorId(), appointmentDate, appointmentTime);
        final var expectedResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, appointmentToDoctorRequest.getDoctorId(),
                expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());
        final var actualResponse = appointmentToDoctor(patientSessionId, appointmentToDoctorRequest);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void denyAppointmentToDoctorTest() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        final var duration = 15;
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = LocalDate.of(2020, 3, 31);
        final var timeStart = LocalTime.of(8, 0);
        final var timeEnd = LocalTime.of(17, 0);
        final var weekDays = List.of(1, 2, 3);

        final var generatedWeekSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(
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

        logout(rootAdminSessionId);

        final var patientRegistrationRequest = new PatientRegistrationDtoRequest("Пахон", "Петров",
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

        final var patientSessionId = patientRegistrationData.getKey();

        final var appointmentDate = LocalDate.of(2020, 3, 17);
        final var appointmentTime = LocalTime.of(10, 15);
        final var appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                expectedDoctorRegistrationResponse.getId(), appointmentDate.toString(), appointmentTime.toString());

        final var expectedTicket = getTicketFactory().buildTicketToDoctor(appointmentToDoctorRequest.getDoctorId(), appointmentDate, appointmentTime);
        final var expectedResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, appointmentToDoctorRequest.getDoctorId(),
                expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());

        final var actualResponse = appointmentToDoctor(patientSessionId, appointmentToDoctorRequest);
        Assertions.assertEquals(expectedResponse, actualResponse);

        denyTicketToDoctor(patientSessionId, expectedTicket);
    }

    @Test
    public void appointmentToDoctorTwoTimesTest() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        final var duration = 15;
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = LocalDate.of(2020, 3, 31);
        final var timeStart = LocalTime.of(8, 0);
        final var timeEnd = LocalTime.of(17, 0);
        final var weekDays = List.of(1, 2, 3);

        final var generatedWeekSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(
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

        logout(rootAdminSessionId);

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

        final var patientSessionId = patientRegistrationData.getKey();

        final var appointmentDate = LocalDate.of(2020, 3, 17);
        final var appointmentTime = LocalTime.of(10, 15);
        final var appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                expectedDoctorRegistrationResponse.getId(), appointmentDate.toString(), appointmentTime.toString());

        final var expectedTicket = getTicketFactory().buildTicketToDoctor(appointmentToDoctorRequest.getDoctorId(), appointmentDate, appointmentTime);
        final var expectedResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, appointmentToDoctorRequest.getDoctorId(),
                expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());

        final var actualResponse = appointmentToDoctor(patientSessionId, appointmentToDoctorRequest);
        Assertions.assertEquals(expectedResponse, actualResponse);

        final var url = buildUrl(PatientController.PREFIX_URL, PatientController.APPOINTMENT_TO_DOCTOR_URL);
        final var json = mapToJson(appointmentToDoctorRequest);

        final var actualJsonResponse = getMvc().perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, patientSessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var actualErrorsDtoResponse = mapFromJson(actualJsonResponse, ErrorsDtoResponse.class);
        final var expectedErrorsDtoResponse = new ErrorsDtoResponse(Collections.singletonList(
                new ErrorDtoResponse(ScheduleErrorCode.ALREADY_CONTAINS_APPOINTMENT.getErrorCode(), "date and time", ScheduleErrorCode.ALREADY_CONTAINS_APPOINTMENT.getErrorMessage())));
        Assertions.assertEquals(expectedErrorsDtoResponse, actualErrorsDtoResponse);
    }

    @Test
    public void appointmentToDoctorBySpecialityTest1() throws Exception {
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

        final var patientSessionId = patientRegistrationData.getKey();

        final var appointmentDate = LocalDate.of(2020, 3, 17);
        final var appointmentTime = LocalTime.of(10, 15);
        final AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                "Dentist", appointmentDate.toString(), appointmentTime.toString());

        final var url = buildUrl(PatientController.PREFIX_URL, PatientController.APPOINTMENT_TO_DOCTOR_URL);
        final var json = mapToJson(appointmentToDoctorRequest);

        final var actualJsonResponse = getMvc().perform(
                MockMvcRequestBuilders
                        .patch(url)
                        .cookie(new Cookie(CookieFactory.JAVA_SESSION_ID, patientSessionId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(actualJsonResponse.isEmpty());

        final var actualErrorsDtoResponse = mapFromJson(actualJsonResponse, ErrorsDtoResponse.class);
        final var expectedErrorsDtoResponse = new ErrorsDtoResponse(Collections.singletonList(
                new ErrorDtoResponse(DoctorNotFoundErrorCode.DOCTOR_NOT_FOUND.getErrorCode(), "id or speciality", DoctorNotFoundErrorCode.DOCTOR_NOT_FOUND.getErrorMessage())));
        Assertions.assertEquals(expectedErrorsDtoResponse, actualErrorsDtoResponse);
    }

    @Test
    public void appointmentToDoctorBySpecialityTest2() throws Exception {
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

        logout(rootAdminSessionId);

        final var patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Пахон", "Петров",
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

        final var patientSessionId = patientRegistrationData.getKey();

        final var appointmentDate = LocalDate.of(2020, 3, 17);
        final var appointmentTime = LocalTime.of(10, 15);
        final AppointmentToDoctorDtoRequest appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(
                expectedDoctorRegistrationResponse.getSpeciality(), appointmentDate.toString(), appointmentTime.toString());

        final var expectedTicket = getTicketFactory().buildTicketToDoctor(expectedDoctorRegistrationResponse.getId(), appointmentDate, appointmentTime);
        final var expectedResponse = new AppointmentToDoctorDtoResponse(
                expectedTicket, expectedDoctorRegistrationResponse.getId(),
                expectedDoctorRegistrationResponse.getFirstName(), expectedDoctorRegistrationResponse.getLastName(), expectedDoctorRegistrationResponse.getPatronymic(),
                expectedDoctorRegistrationResponse.getSpeciality(), expectedDoctorRegistrationResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString());
        final var actualResponse = appointmentToDoctor(patientSessionId, appointmentToDoctorRequest);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void appointmentToDoctorBySpecialityTest3() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        final var duration = 15;
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = LocalDate.of(2020, 3, 31);
        final var timeStart = LocalTime.of(8, 0);
        final var timeEnd = LocalTime.of(17, 0);
        final var weekDays = List.of(1, 2, 3);
        final var speciality = "Surgeon";
        final var responses = new HashMap<Integer, DoctorRegistrationDtoResponse>();

        final var generatedWeekSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(
                duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        {
            final var doctorRegistrationRequest = new DoctorRegistrationDtoRequest(
                    generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                    generatedWeekSchedule.getWeekSchedule(),
                    "Саркис", "Семёнов", "Вениаминович",
                    speciality, "205", "SarkisSemenov585", "xjNE6QK6d3b9");
            final var actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
            final var expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                    doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                    doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
            Assertions.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);
            responses.put(expectedDoctorRegistrationResponse.getId(), expectedDoctorRegistrationResponse);
        }

        {
            final var doctorRegistrationRequest = new DoctorRegistrationDtoRequest(
                    generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                    generatedWeekSchedule.getWeekDaysSchedule(),
                    "Аксырга", "Прокофьева", "Леонидовна",
                    speciality, "306", "AksyrgaProkofeva993", "TiW0wvq2HNaB");
            final var actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
            final var expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                    doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                    doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
            Assertions.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);
            responses.put(expectedDoctorRegistrationResponse.getId(), expectedDoctorRegistrationResponse);
        }

        {
            final var doctorRegistrationRequest = new DoctorRegistrationDtoRequest(
                    generatedWeekSchedule.getDateStart(), generatedWeekSchedule.getDateEnd(), generatedWeekSchedule.getDuration(),
                    generatedWeekSchedule.getWeekDaysSchedule(),
                    "Чилина", "Царева", "Ермаковна",
                    speciality, "261", "ChilinaTsareva849", "9LZfrHND9Nrq");
            final var actualDoctorRegistrationResponse = doctorRegistration(rootAdminSessionId, doctorRegistrationRequest);
            final var expectedDoctorRegistrationResponse = new DoctorRegistrationDtoResponse(actualDoctorRegistrationResponse.getId(),
                    doctorRegistrationRequest.getFirstName(), doctorRegistrationRequest.getLastName(), doctorRegistrationRequest.getPatronymic(),
                    doctorRegistrationRequest.getSpeciality(), doctorRegistrationRequest.getRoom(), actualDoctorRegistrationResponse.getSchedule());
            Assertions.assertEquals(expectedDoctorRegistrationResponse, actualDoctorRegistrationResponse);
            responses.put(expectedDoctorRegistrationResponse.getId(), expectedDoctorRegistrationResponse);
        }

        logout(rootAdminSessionId);

        final var patientRegistrationRequest = new PatientRegistrationDtoRequest("Пахон", "Петров",
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

        final var patientSessionId = patientRegistrationData.getKey();

        final var appointmentDate = LocalDate.of(2020, 3, 17);
        final var appointmentTime = LocalTime.of(10, 15);
        final var appointmentToDoctorRequest = new AppointmentToDoctorDtoRequest(speciality, appointmentDate.toString(), appointmentTime.toString());

        final var actualResponse = appointmentToDoctor(patientSessionId, appointmentToDoctorRequest);
        final var docResponse = responses.get(actualResponse.getDoctorId());

        final var expectedTicket = getTicketFactory().buildTicketToDoctor(docResponse.getId(), appointmentDate, appointmentTime);
        final var expectedResponse = new AppointmentToDoctorDtoResponse(expectedTicket, docResponse.getId(),
                docResponse.getFirstName(), docResponse.getLastName(), docResponse.getPatronymic(),
                docResponse.getSpeciality(), docResponse.getRoom(), appointmentDate.toString(), appointmentTime.toString()
        );

        Assertions.assertEquals(expectedResponse, actualResponse);
    }
}