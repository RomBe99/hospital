package net.thumbtack.hospital.controller.mock;

import net.thumbtack.hospital.controller.api.MockedControllerTestApi;
import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
public class DoctorMockedControllerTest extends MockedControllerTestApi {
    private final Function<CreateMedicalCommissionDtoRequest, CreateMedicalCommissionDtoResponse> MEDICAL_COMMISSION_REQUEST_TO_RESPONSE_TRANSFORMER =
            dtoRequest -> {
                final var doctorIds = dtoRequest.getDoctorIds();
                final var date = LocalDate.parse(dtoRequest.getDate());
                final var time = LocalTime.parse(dtoRequest.getTime());
                final var ticketTitle = getTicketFactory().buildTicketToCommission(date, time, doctorIds);

                return new CreateMedicalCommissionDtoResponse(ticketTitle, dtoRequest.getPatientId(), doctorIds,
                        dtoRequest.getRoom(), dtoRequest.getDate(), dtoRequest.getTime(), dtoRequest.getDuration());
            };

    @Test
    public void createAndDenyMedicalCommission() throws Exception {
        final var rootAdminSessionId = loginRootAdmin();

        final var doctorCreatedCommission = new DoctorRegistrationDtoRequest(
                LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, Collections.emptyList(),
                "Есенгалий", "Скачков", null,
                "Surgeon", "104", "EsengaliySkachkov56", "u15fv6vy5NvA");

        final var doctorRegistrationRequests = List.of(
                doctorCreatedCommission,
                new DoctorRegistrationDtoRequest(
                        LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, Collections.emptyList(),
                        "Епихария", "Козлова", null,
                        "Surgeon", "124", "EpihariyaKozlova75", "44XNaexggtgK"),
                new DoctorRegistrationDtoRequest(
                        LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, Collections.emptyList(),
                        "Кариадна", "Шарыпова", null,
                        "Surgeon", "205", "KariadnaSharypova754", "1Ur1QSHY0lA0"),
                new DoctorRegistrationDtoRequest(
                        LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, Collections.emptyList(),
                        "Риолана", "Садовничая", null,
                        "Surgeon", "261", "RiolanaSadovnichaya144", "nqBxvSnejwa3"),
                new DoctorRegistrationDtoRequest(
                        LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, Collections.emptyList(),
                        "Хамнам", "Андрианов", null,
                        "Surgeon", "306", "HamnamAndrianov239", "6Ok5lrf2vdpI")
        );
        final var doctorRegistrationResponses = new HashMap<DoctorRegistrationDtoRequest, DoctorRegistrationDtoResponse>();

        for (var r : doctorRegistrationRequests) {
            final var actualResponse = doctorRegistration(rootAdminSessionId, r);
            final var expectedResponse = new DoctorRegistrationDtoResponse(actualResponse.getId(),
                    r.getFirstName(), r.getLastName(), r.getPatronymic(),
                    r.getSpeciality(), r.getRoom(), actualResponse.getSchedule());
            Assertions.assertEquals(expectedResponse, actualResponse);

            doctorRegistrationResponses.put(r, expectedResponse);

        }

        final var doctorCreatedCommissionData = login(doctorCreatedCommission.getLogin(), doctorCreatedCommission.getPassword(), DoctorLoginDtoResponse.class);

        {
            final var actualResponse = doctorCreatedCommissionData.getValue();
            final var expectedResponse = new DoctorLoginDtoResponse(actualResponse.getId(),
                    doctorCreatedCommission.getFirstName(), doctorCreatedCommission.getLastName(), doctorCreatedCommission.getPatronymic(),
                    doctorCreatedCommission.getSpeciality(), doctorCreatedCommission.getRoom(), Collections.emptyList());

            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        final var patientRegistrationRequest = new PatientRegistrationDtoRequest("Сеитибрам", "Ивлеев", "ftl4a@amazingmail.xyz", "423973, г. Саров (Морд.), ул. Новоподмосковный 6-й пер, дом 146, квартира 687", "+7 (922) 183-51-08", "SeitibramIvleev493", "DOCexbGeEIS7");
        final var patientRegistrationData = patientRegistration(patientRegistrationRequest);
        final var patientId = patientRegistrationData.getValue().getId();

        {
            final var actualResponse = patientRegistrationData.getValue();
            final var expectedResponse = new PatientRegistrationDtoResponse(patientId,
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), "+79221835108");

            Assertions.assertEquals(expectedResponse, actualResponse);
        }
        final var doctorIds = doctorRegistrationResponses.values().stream()
                .map(DoctorRegistrationDtoResponse::getId)
                .collect(Collectors.toList());

        final var createMedicalCommissionRequest = new CreateMedicalCommissionDtoRequest(patientId, doctorIds, "124",
                LocalDate.of(2020, 4, 5).toString(), LocalTime.of(12, 0).toString(), 120);
        {
            final var actualResponse = createMedicalCommission(doctorCreatedCommissionData.getKey(), createMedicalCommissionRequest);
            final var expectedResponse = MEDICAL_COMMISSION_REQUEST_TO_RESPONSE_TRANSFORMER.apply(createMedicalCommissionRequest);
            Assertions.assertEquals(expectedResponse, actualResponse);
        }

        final var ticketTitle = getTicketFactory().buildTicketToCommission(LocalDate.parse(createMedicalCommissionRequest.getDate()),
                LocalTime.parse(createMedicalCommissionRequest.getTime()), createMedicalCommissionRequest.getDoctorIds());

        {
            final var actualResponse = denyTicketToDoctor(patientRegistrationData.getKey(), ticketTitle);
            final var expectedResponse = new EmptyDtoResponse();

            Assertions.assertEquals(expectedResponse, actualResponse);
        }
    }
}