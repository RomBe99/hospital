package net.thumbtack.hospital.controller.integration;

import net.thumbtack.hospital.controller.api.RealControllerTestApi;
import net.thumbtack.hospital.dtorequest.admin.DoctorRegistrationDtoRequest;
import net.thumbtack.hospital.dtorequest.doctor.CreateMedicalCommissionDtoRequest;
import net.thumbtack.hospital.dtorequest.patient.PatientRegistrationDtoRequest;
import net.thumbtack.hospital.dtoresponse.admin.DoctorRegistrationDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.CreateMedicalCommissionDtoResponse;
import net.thumbtack.hospital.dtoresponse.doctor.DoctorLoginDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.EmptyDtoResponse;
import net.thumbtack.hospital.dtoresponse.patient.PatientRegistrationDtoResponse;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
public class DoctorControllerTest extends RealControllerTestApi {
    private static final Function<CreateMedicalCommissionDtoRequest, CreateMedicalCommissionDtoResponse> MEDICAL_COMMISSION_REQUEST_TO_RESPONSE_TRANSFORMER =
            dtoRequest -> {
                final List<Integer> doctorIds = dtoRequest.getDoctorIds();
                final LocalDate date = LocalDate.parse(dtoRequest.getDate());
                final LocalTime time = LocalTime.parse(dtoRequest.getTime());
                final String ticketTitle = TicketFactory.buildTicketToCommission(date, time, doctorIds);

                return new CreateMedicalCommissionDtoResponse(ticketTitle, dtoRequest.getPatientId(), doctorIds,
                        dtoRequest.getRoom(), dtoRequest.getDate(), dtoRequest.getTime(), dtoRequest.getDuration());
            };

    @Test
    public void createAndDenyMedicalCommission() throws Exception {
        final String rootAdminSessionId = loginRootAdmin();

        final DoctorRegistrationDtoRequest doctorCreatedCommission = new DoctorRegistrationDtoRequest(
                LocalDate.of(2020, 3, 1).toString(), LocalDate.of(2020, 3, 2).toString(), 15, Collections.emptyList(),
                "Есенгалий", "Скачков", null,
                "Surgeon", "104", "EsengaliySkachkov56", "u15fv6vy5NvA");

        final List<DoctorRegistrationDtoRequest> doctorRegistrationRequests = Arrays.asList(
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
        final Map<DoctorRegistrationDtoRequest, DoctorRegistrationDtoResponse> doctorRegistrationResponses = new HashMap<>();

        for (DoctorRegistrationDtoRequest r : doctorRegistrationRequests) {
            final DoctorRegistrationDtoResponse actualResponse = doctorRegistration(rootAdminSessionId, r);
            final DoctorRegistrationDtoResponse expectedResponse = new DoctorRegistrationDtoResponse(actualResponse.getId(),
                    r.getFirstName(), r.getLastName(), r.getPatronymic(),
                    r.getSpeciality(), r.getRoom(), actualResponse.getSchedule());
            Assert.assertEquals(expectedResponse, actualResponse);

            doctorRegistrationResponses.put(r, expectedResponse);

        }

        final Pair<String, DoctorLoginDtoResponse> doctorCreatedCommissionData = login(doctorCreatedCommission.getLogin(), doctorCreatedCommission.getPassword(), DoctorLoginDtoResponse.class);

        {
            final DoctorLoginDtoResponse actualResponse = doctorCreatedCommissionData.getValue();
            final DoctorLoginDtoResponse expectedResponse = new DoctorLoginDtoResponse(actualResponse.getId(),
                    doctorCreatedCommission.getFirstName(), doctorCreatedCommission.getLastName(), doctorCreatedCommission.getPatronymic(),
                    doctorCreatedCommission.getSpeciality(), doctorCreatedCommission.getRoom(), Collections.emptyList());

            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final PatientRegistrationDtoRequest patientRegistrationRequest = new PatientRegistrationDtoRequest("Сеитибрам", "Ивлеев", "ftl4a@amazingmail.xyz", "423973, г. Саров (Морд.), ул. Новоподмосковный 6-й пер, дом 146, квартира 687", "+7 (922) 183-51-08", "SeitibramIvleev493", "DOCexbGeEIS7");
        final Pair<String, PatientRegistrationDtoResponse> patientRegistrationData = patientRegistration(patientRegistrationRequest);
        final int patientId = patientRegistrationData.getValue().getId();

        {
            final PatientRegistrationDtoResponse actualResponse = patientRegistrationData.getValue();
            final PatientRegistrationDtoResponse expectedResponse = new PatientRegistrationDtoResponse(patientId,
                    patientRegistrationRequest.getFirstName(), patientRegistrationRequest.getLastName(), patientRegistrationRequest.getPatronymic(),
                    patientRegistrationRequest.getEmail(), patientRegistrationRequest.getAddress(), "+79221835108");

            Assert.assertEquals(expectedResponse, actualResponse);
        }
        final List<Integer> doctorIds = doctorRegistrationResponses.values().stream().map(DoctorRegistrationDtoResponse::getId).collect(Collectors.toList());

        final CreateMedicalCommissionDtoRequest createMedicalCommissionRequest =
                new CreateMedicalCommissionDtoRequest(patientId, doctorIds, "124", LocalDate.of(2020, 4, 5).toString(), LocalTime.of(12, 0).toString(), 120);
        {
            final CreateMedicalCommissionDtoResponse actualResponse = createMedicalCommission(doctorCreatedCommissionData.getKey(), createMedicalCommissionRequest);
            final CreateMedicalCommissionDtoResponse expectedResponse = MEDICAL_COMMISSION_REQUEST_TO_RESPONSE_TRANSFORMER.apply(createMedicalCommissionRequest);
            Assert.assertEquals(expectedResponse, actualResponse);
        }

        final String ticketTitle = TicketFactory.buildTicketToCommission(LocalDate.parse(createMedicalCommissionRequest.getDate()),
                LocalTime.parse(createMedicalCommissionRequest.getTime()), createMedicalCommissionRequest.getDoctorIds());

        {
            final EmptyDtoResponse actualResponse = denyTicketToDoctor(patientRegistrationData.getKey(), ticketTitle);
            final EmptyDtoResponse expectedResponse = new EmptyDtoResponse();

            Assert.assertEquals(expectedResponse, actualResponse);
        }
    }
}