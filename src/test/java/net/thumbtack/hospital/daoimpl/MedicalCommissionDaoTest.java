package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalCommissionDaoTest extends DaoTestApi {
    @Test
    public void createMedicalCommissionTest() {
        final var doctorCreatedCommission = new Doctor("MenaAltyrev568", "ceVCCj14uw4H",
                "Варсеник", "Чудина", "Ивановна", "342", "Therapist", Collections.emptyList());

        final var doctors = List.of(doctorCreatedCommission,
                new Doctor("ArnestinaChehova823", "l1E57ydLV9M1",
                        "Арнестина", "Чехова", "Ефимовна", "104", "Dentist", Collections.emptyList()),
                new Doctor("KostantinVolochkov999", "d8Nb22tloSMQ",
                        "Костантин", "Ильич", null, "205", "Surgeon", Collections.emptyList()),
                new Doctor("AzhinaSolntseva451", "2I7S5a7ze911",
                        "Ажина", "Солнцева", "Олеговна", "306", "Therapist", Collections.emptyList()),
                new Doctor("BalmuradOblomov794", "TwQXwwyAJy64",
                        "Балмурад", "Обломов", "Андреевич", "471", "Traumatologist", Collections.emptyList()),
                new Doctor("MagamedzakirSaharov326", "9V3JaO1Ollde",
                        "Магамедзакир", "Сахаров", "Анатольевич", "124", "Dentist", Collections.emptyList()));

        for (var d : doctors) {
            insertUser(d);
        }

        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        final var commissionDate = LocalDate.of(2020, 4, 7);
        final var commissionTime = LocalTime.of(14, 30);
        final var doctorIds = doctors.stream().map(User::getId).collect(Collectors.toList());
        final var ticketTitle = getTicketFactory().buildTicketToCommission(commissionDate, commissionTime, doctorIds);
        final var cabinet = doctorCreatedCommission.getCabinet();
        final var patientId = patient.getId();
        final var duration = 120;

        final var ticket = new TicketToMedicalCommission(ticketTitle, cabinet, commissionDate, commissionTime,
                patientId, doctorIds, duration);
        createMedicalCommission(ticket);
    }

    @Test
    public void denyMedicalCommissionTest() {
        final var doctorCreatedCommission = new Doctor("MenaAltyrev568", "ceVCCj14uw4H",
                "Варсеник", "Чудина", "Ивановна", "342", "Therapist", Collections.emptyList());

        final var doctors = List.of(doctorCreatedCommission,
                new Doctor("ArnestinaChehova823", "l1E57ydLV9M1",
                        "Арнестина", "Чехова", "Ефимовна", "104", "Dentist", Collections.emptyList()),
                new Doctor("KostantinVolochkov999", "d8Nb22tloSMQ",
                        "Костантин", "Ильич", null, "205", "Surgeon", Collections.emptyList()),
                new Doctor("AzhinaSolntseva451", "2I7S5a7ze911",
                        "Ажина", "Солнцева", "Олеговна", "306", "Therapist", Collections.emptyList()),
                new Doctor("BalmuradOblomov794", "TwQXwwyAJy64",
                        "Балмурад", "Обломов", "Андреевич", "471", "Traumatologist", Collections.emptyList()),
                new Doctor("MagamedzakirSaharov326", "9V3JaO1Ollde",
                        "Магамедзакир", "Сахаров", "Анатольевич", "124", "Dentist", Collections.emptyList()));

        for (var d : doctors) {
            insertUser(d);
        }

        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        final var commissionDate = LocalDate.of(2020, 4, 7);
        final var commissionTime = LocalTime.of(14, 30);
        final var doctorIds = doctors.stream().map(User::getId).collect(Collectors.toList());
        final var ticketTitle = getTicketFactory().buildTicketToCommission(commissionDate, commissionTime, doctorIds);
        final var cabinet = doctorCreatedCommission.getCabinet();
        final var patientId = patient.getId();
        final var duration = 120;

        final var ticket = new TicketToMedicalCommission(ticketTitle, cabinet, commissionDate, commissionTime,
                patientId, doctorIds, duration);
        createMedicalCommission(ticket);
        denyMedicalCommission(ticketTitle);
    }

    @Test
    public void getEmptyListTicketsToMedicalCommissionTest() {
        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        getTicketsToMedicalCommission(patient.getId(), Collections.emptyList());
    }

    @Test
    public void getTicketsToMedicalCommissionTest() {
        final var doctors = List.of(
                new Doctor("MenaAltyrev568", "ceVCCj14uw4H",
                        "Варсеник", "Чудина", "Ивановна", "342", "Therapist", Collections.emptyList()),
                new Doctor("ArnestinaChehova823", "l1E57ydLV9M1",
                        "Арнестина", "Чехова", "Ефимовна", "104", "Dentist", Collections.emptyList()),
                new Doctor("KostantinVolochkov999", "d8Nb22tloSMQ",
                        "Костантин", "Ильич", null, "205", "Surgeon", Collections.emptyList()),
                new Doctor("AzhinaSolntseva451", "2I7S5a7ze911",
                        "Ажина", "Солнцева", "Олеговна", "306", "Therapist", Collections.emptyList()),
                new Doctor("BalmuradOblomov794", "TwQXwwyAJy64",
                        "Балмурад", "Обломов", "Андреевич", "471", "Traumatologist", Collections.emptyList()),
                new Doctor("MagamedzakirSaharov326", "9V3JaO1Ollde",
                        "Магамедзакир", "Сахаров", "Анатольевич", "124", "Dentist", Collections.emptyList()))
                .stream().collect(Collectors.toMap(User::getLogin, d -> d));

        for (var d : doctors.values()) {
            insertUser(d);
        }

        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        // Create some commissions

        var doctorCreatedCommission = doctors.get("MagamedzakirSaharov326");
        var commissionDate = LocalDate.of(2020, 4, 7);
        var commissionTime = LocalTime.of(14, 30);
        var doctorIds = doctors.values().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        var ticketTitle = getTicketFactory().buildTicketToCommission(commissionDate, commissionTime, doctorIds);
        var cabinet = doctorCreatedCommission.getCabinet();
        var patientId = patient.getId();
        var duration = 120;

        final var expectedTickets = new ArrayList<TicketToMedicalCommission>();
        expectedTickets.add(new TicketToMedicalCommission(ticketTitle, cabinet, commissionDate, commissionTime, patientId, doctorIds, duration));

        doctorCreatedCommission = doctors.get("AzhinaSolntseva451");
        commissionDate = LocalDate.of(2020, 3, 25);
        commissionTime = LocalTime.of(13, 0);
        doctorIds = List.of(doctors.get("MenaAltyrev568"), doctors.get("ArnestinaChehova823"), doctorCreatedCommission)
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
        ticketTitle = getTicketFactory().buildTicketToCommission(commissionDate, commissionTime, doctorIds);
        cabinet = doctors.get("MenaAltyrev568").getCabinet();
        duration = 30;
        expectedTickets.add(new TicketToMedicalCommission(ticketTitle, cabinet, commissionDate, commissionTime, patientId, doctorIds, duration));

        doctorCreatedCommission = doctors.get("BalmuradOblomov794");
        commissionDate = LocalDate.of(2020, 5, 10);
        commissionTime = LocalTime.of(8, 0);
        doctorIds = List.of(doctors.get("AzhinaSolntseva451"), doctorCreatedCommission)
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
        ticketTitle = getTicketFactory().buildTicketToCommission(commissionDate, commissionTime, doctorIds);
        cabinet = doctorCreatedCommission.getCabinet();
        duration = 100;
        expectedTickets.add(new TicketToMedicalCommission(ticketTitle, cabinet, commissionDate, commissionTime, patientId, doctorIds, duration));

        for (var t : expectedTickets) {
            createMedicalCommission(t);
        }

        getTicketsToMedicalCommission(patientId, expectedTickets);
    }

    @Test
    public void insertTicketToMedicalCommissionWithNullTitleTest() {
        final var doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", Collections.emptyList());
        insertUser(doctor);

        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        final var incorrectTicket = new TicketToMedicalCommission(null, doctor.getCabinet(),
                LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), patient.getId(),
                Collections.singletonList(doctor.getId()), 120);

        Assertions.assertThrows(RuntimeException.class, () -> createMedicalCommission(incorrectTicket));
    }

    @Test
    public void insertTicketToMedicalCommissionWithIncorrectCabinetTest() {
        final var doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", Collections.emptyList());
        insertUser(doctor);

        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        final var incorrectTicket = new TicketToMedicalCommission("Ticket title", "Incorrect cabinet",
                LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), patient.getId(),
                Collections.singletonList(doctor.getId()), 120);

        Assertions.assertThrows(RuntimeException.class, () -> createMedicalCommission(incorrectTicket));
    }

    @Test
    public void insertTicketToMedicalCommissionWithNullCabinetTest() {
        final var doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", Collections.emptyList());
        insertUser(doctor);

        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        final var incorrectTicket = new TicketToMedicalCommission("Ticket title", null,
                LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), patient.getId(),
                Collections.singletonList(doctor.getId()), 120);

        Assertions.assertThrows(RuntimeException.class, () -> createMedicalCommission(incorrectTicket));
    }

    @Test
    public void insertTicketToMedicalCommissionWithNullDateTest() {
        final var doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", Collections.emptyList());
        insertUser(doctor);

        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        final var incorrectTicket = new TicketToMedicalCommission("Ticket title", doctor.getCabinet(),
                null, LocalTime.of(8, 0), patient.getId(),
                Collections.singletonList(doctor.getId()), 120);

        Assertions.assertThrows(RuntimeException.class, () -> createMedicalCommission(incorrectTicket));
    }

    @Test
    public void insertTicketToMedicalCommissionWithNullTimeTest() {
        final var doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", Collections.emptyList());
        insertUser(doctor);

        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        final var incorrectTicket = new TicketToMedicalCommission("Ticket title", doctor.getCabinet(),
                LocalDate.of(2020, 3, 4), null, patient.getId(),
                Collections.singletonList(doctor.getId()), 120);

        Assertions.assertThrows(RuntimeException.class, () -> createMedicalCommission(incorrectTicket));
    }

    @Test
    public void insertTicketToMedicalCommissionWithIncorrectPatientIdTest() {
        final var doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", Collections.emptyList());
        insertUser(doctor);

        final var incorrectTicket = new TicketToMedicalCommission("Ticket title", doctor.getCabinet(),
                LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), -1,
                Collections.singletonList(doctor.getId()), 120);

        Assertions.assertThrows(RuntimeException.class, () -> createMedicalCommission(incorrectTicket));
    }

    @Test
    public void insertTicketToMedicalCommissionWithEmptyDoctorIdsTest() {
        final var doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", Collections.emptyList());
        insertUser(doctor);

        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        final var incorrectTicket = new TicketToMedicalCommission("Ticket title", doctor.getCabinet(),
                LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), patient.getId(),
                Collections.emptyList(), 120);

        Assertions.assertThrows(RuntimeException.class, () -> createMedicalCommission(incorrectTicket));
    }

    @Test
    public void insertTicketToMedicalCommissionWithIncorrectDoctorIdsTest() {
        final var doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", Collections.emptyList());
        insertUser(doctor);

        final var patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        final var incorrectTicket = new TicketToMedicalCommission("Ticket title", doctor.getCabinet(),
                LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), patient.getId(),
                List.of(-1, -2), 120);

        Assertions.assertThrows(RuntimeException.class, () -> createMedicalCommission(incorrectTicket));
    }
}