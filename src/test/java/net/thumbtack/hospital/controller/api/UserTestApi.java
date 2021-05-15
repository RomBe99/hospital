package net.thumbtack.hospital.controller.api;

import net.thumbtack.hospital.dtoresponse.admin.AdminLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.LoginUserDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.SettingsDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.abstractresponse.UserInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.user.GetAllDoctorsDtoResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;

public interface UserTestApi {
    <T extends LoginUserDtoResponse> Pair<String, T> login(String login, String password, Class<T> clazz) throws Exception;

    default String loginRootAdmin() throws Exception {
        final var login = "admin";
        final var password = "admin";

        final var loginData = login(login, password, AdminLoginDtoResponse.class);
        final var expectedResponse = new AdminLoginDtoResponse(loginData.getValue().getId(),
                "Roman", "Belinsky", null, "Root admin");
        Assertions.assertEquals(expectedResponse, loginData.getValue());

        return loginData.getKey();
    }

    void logout(String sessionId) throws Exception;

    <T extends UserInformationDtoResponse> T getUserInformation(String sessionId, Class<T> clazz) throws Exception;

    DoctorInformationDtoResponse getDoctorInformation(String sessionId, int doctorId,
                                                      String schedule, String startDate, String endDate) throws Exception;

    GetAllDoctorsDtoResponse getDoctorsInformation(String sessionId, String schedule, String speciality,
                                                   String startDate, String endDate) throws Exception;

    PatientInformationDtoResponse getPatientInformation(String sessionId, int patientId) throws Exception;

    <T extends SettingsDtoResponse> T getSettings(String sessionId, Class<T> clazz) throws Exception;
}