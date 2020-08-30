package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.patient.EditPatientProfileDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.patient.EditPatientProfileDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.FullPatientInformationDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

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
}