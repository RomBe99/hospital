package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.configuration.Constraints;
import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ServerSettingsDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.SettingsDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.util.ScheduleGenerators;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class UserControllerTest extends ControllerTestApi {
    @Autowired
    private Constraints constraints;

    @Test
    public void getAdministratorSettings() throws Exception {
        String rootAdminSessionId = loginRootAdmin();

        SettingsDtoResponse expectedResponse =
                new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength());

        getSettings(rootAdminSessionId, expectedResponse);
    }

    @Test
    public void getDoctorSettings() throws Exception {
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

        SettingsDtoResponse expectedResponse =
                new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength());

        getSettings(doctorSessionId, expectedResponse);
    }

    @Test
    public void getPatientSettings() throws Exception {
        PatientRegistrationDtoRequest patientRegistrationRequest =
                new PatientRegistrationDtoRequest("Пахон", "Петров",
                        "camikaf920@mijumail.com", "617823, г. Усмань, ул. Гаккелевская, дом 153, квартира 346", "+7 (922) 656-58-24",
                        "PahonPetrov927", "ugSfPaGD1YBv");

        String patientSessionId = patientRegistration(patientRegistrationRequest,
                new PatientRegistrationDtoResponse(patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                        patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), patientRegistrationRequest.getPhone()));

        SettingsDtoResponse expectedResponse =
                new ServerSettingsDtoResponse(constraints.getMaxNameLength(), constraints.getMinPasswordLength());

        getSettings(patientSessionId, expectedResponse);
    }
}