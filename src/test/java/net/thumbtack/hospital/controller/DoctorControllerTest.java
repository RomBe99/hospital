package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
public class DoctorControllerTest extends ControllerTestApi {
    private final static Function<DoctorRegistrationDtoRequest, DoctorRegistrationDtoResponse> DOCTOR_REQUEST_TO_RESPONSE_TRANSFORMER =
            dtoRequest -> new DoctorRegistrationDtoResponse(dtoRequest.getFirstName(), dtoRequest.getLastName(), dtoRequest.getPatronymic(),
                    dtoRequest.getSpeciality(), dtoRequest.getRoom(), new ArrayList<>());
    private final static Function<CreateMedicalCommissionDtoRequest, CreateMedicalCommissionDtoResponse> MEDICAL_COMMISSION_REQUEST_TO_RESPONSE_TRANSFORMER =
            dtoRequest -> {
                List<Integer> doctorIds = dtoRequest.getDoctorIds();
                LocalDate date = LocalDate.parse(dtoRequest.getDate());
                LocalTime time = LocalTime.parse(dtoRequest.getTime());
                String ticketTitle = TicketFactory.buildTicketToCommission(date, time, doctorIds);

                return new CreateMedicalCommissionDtoResponse(ticketTitle, dtoRequest.getPatientId(), doctorIds,
                        dtoRequest.getRoom(), dtoRequest.getDate(), dtoRequest.getTime(), dtoRequest.getDuration());
            };
    private final static Function<DoctorRegistrationDtoRequest, DoctorLoginDtoResponse> DOCTOR_REQUEST_TO_LOGIN_RESPONSE_TRANSFORMER =
            dtoRequest -> new DoctorLoginDtoResponse(dtoRequest.getFirstName(), dtoRequest.getLastName(), dtoRequest.getPatronymic(),
                    dtoRequest.getSpeciality(), dtoRequest.getRoom(), new ArrayList<>());

    @Test
    public void createAndDenyMedicalCommission() throws Exception {
        String rootAdminSessionId = loginRootAdmin();

        DoctorRegistrationDtoRequest doctorCreatedCommission =
                new DoctorRegistrationDtoRequest(LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, new ArrayList<>(),
                        "Есенгалий", "Скачков", null,
                        "Surgeon", "104", "EsengaliySkachkov56", "u15fv6vy5NvA");

        Map<DoctorRegistrationDtoRequest, DoctorRegistrationDtoResponse> doctorRegistrations = Stream.of(
                doctorCreatedCommission,
                new DoctorRegistrationDtoRequest(LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, new ArrayList<>(),
                        "Епихария", "Козлова", null,
                        "Surgeon", "124", "EpihariyaKozlova75", "44XNaexggtgK"),
                new DoctorRegistrationDtoRequest(LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, new ArrayList<>(),
                        "Кариадна", "Шарыпова", null,
                        "Surgeon", "205", "KariadnaSharypova754", "1Ur1QSHY0lA0"),
                new DoctorRegistrationDtoRequest(LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, new ArrayList<>(),
                        "Риолана", "Садовничая", null,
                        "Surgeon", "261", "RiolanaSadovnichaya144", "nqBxvSnejwa3"),
                new DoctorRegistrationDtoRequest(LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, new ArrayList<>(),
                        "Хамнам", "Андрианов", null,
                        "Surgeon", "306", "HamnamAndrianov239", "6Ok5lrf2vdpI")
        ).collect(Collectors.toMap(r -> r, DOCTOR_REQUEST_TO_RESPONSE_TRANSFORMER));

        for (DoctorRegistrationDtoRequest dtoRequest : doctorRegistrations.keySet()) {
            doctorRegistration(rootAdminSessionId, dtoRequest, doctorRegistrations.get(dtoRequest));
        }

        String doctorCreatedCommissionSessionId = login(doctorCreatedCommission.getLogin(), doctorCreatedCommission.getPassword(),
                DOCTOR_REQUEST_TO_LOGIN_RESPONSE_TRANSFORMER.apply(doctorCreatedCommission));

        PatientRegistrationDtoRequest patientRegistrationRequest = new PatientRegistrationDtoRequest("Сеитибрам", "Ивлеев", "ftl4a@amazingmail.xyz", "423973, г. Саров (Морд.), ул. Новоподмосковный 6-й пер, дом 146, квартира 687", "+7 (922) 183-51-08", "SeitibramIvleev493", "DOCexbGeEIS7");
        PatientRegistrationDtoResponse patientRegistrationResponse = new PatientRegistrationDtoResponse(patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(), patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), "+79221835108");
        String patientSessionId = patientRegistration(patientRegistrationRequest, patientRegistrationResponse);

        int patientId = patientRegistrationResponse.getId();
        List<Integer> doctorIds = doctorRegistrations.values().stream().map(DoctorRegistrationDtoResponse::getId).collect(Collectors.toList());

        CreateMedicalCommissionDtoRequest createMedicalCommissionRequest =
                new CreateMedicalCommissionDtoRequest(patientId, doctorIds, "124", LocalDate.of(2020, 4, 5).toString(), LocalTime.of(12, 0).toString(), 120);
        createMedicalCommission(doctorCreatedCommissionSessionId, createMedicalCommissionRequest,
                MEDICAL_COMMISSION_REQUEST_TO_RESPONSE_TRANSFORMER.apply(createMedicalCommissionRequest));

        String ticketTitle = TicketFactory.buildTicketToCommission(LocalDate.parse(createMedicalCommissionRequest.getDate()),
                LocalTime.parse(createMedicalCommissionRequest.getTime()), createMedicalCommissionRequest.getDoctorIds());
        denyTicketToDoctor(patientSessionId, ticketTitle);
    }
}