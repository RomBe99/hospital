package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.model.user.User;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MedicalCommissionDaoTest extends DaoTestApi {
    @Test
    public void createMedicalCommissionTest() {
        Doctor doctorCreatedCommission = new Doctor("MenaAltyrev568", "ceVCCj14uw4H",
                "Варсеник", "Чудина", "Ивановна", "342", "Therapist", new ArrayList<>());

        List<Doctor> doctors = Arrays.asList(doctorCreatedCommission,
                new Doctor("ArnestinaChehova823", "l1E57ydLV9M1",
                        "Арнестина", "Чехова", "Ефимовна", "104", "Dentist", new ArrayList<>()),
                new Doctor("KostantinVolochkov999", "d8Nb22tloSMQ",
                        "Костантин", "Ильич", null, "205", "Surgeon", new ArrayList<>()),
                new Doctor("AzhinaSolntseva451", "2I7S5a7ze911",
                        "Ажина", "Солнцева", "Олеговна", "306", "Therapist", new ArrayList<>()),
                new Doctor("BalmuradOblomov794", "TwQXwwyAJy64",
                        "Балмурад", "Обломов", "Андреевич", "471", "Traumatologist", new ArrayList<>()),
                new Doctor("MagamedzakirSaharov326", "9V3JaO1Ollde",
                        "Магамедзакир", "Сахаров", "Анатольевич", "124", "Dentist", new ArrayList<>()));

        for (Doctor d : doctors) {
            insertUser(d);
        }

        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        LocalDate commissionDate = LocalDate.of(2020, 4, 7);
        LocalTime commissionTime = LocalTime.of(14, 30);
        List<Integer> doctorIds = doctors.stream().map(User::getId).collect(Collectors.toList());
        String ticketTitle = TicketFactory.buildTicketToCommission(commissionDate, commissionTime, doctorIds);
        String cabinet = doctorCreatedCommission.getCabinet();
        int patientId = patient.getId();
        int duration = 120;

        TicketToMedicalCommission ticket =
                new TicketToMedicalCommission(ticketTitle, cabinet, commissionDate, commissionTime, patientId, doctorIds, duration);
        createMedicalCommission(ticket);
    }

    @Test
    public void denyMedicalCommissionTest() {
        Doctor doctorCreatedCommission = new Doctor("MenaAltyrev568", "ceVCCj14uw4H",
                "Варсеник", "Чудина", "Ивановна", "342", "Therapist", new ArrayList<>());

        List<Doctor> doctors = Arrays.asList(doctorCreatedCommission,
                new Doctor("ArnestinaChehova823", "l1E57ydLV9M1",
                        "Арнестина", "Чехова", "Ефимовна", "104", "Dentist", new ArrayList<>()),
                new Doctor("KostantinVolochkov999", "d8Nb22tloSMQ",
                        "Костантин", "Ильич", null, "205", "Surgeon", new ArrayList<>()),
                new Doctor("AzhinaSolntseva451", "2I7S5a7ze911",
                        "Ажина", "Солнцева", "Олеговна", "306", "Therapist", new ArrayList<>()),
                new Doctor("BalmuradOblomov794", "TwQXwwyAJy64",
                        "Балмурад", "Обломов", "Андреевич", "471", "Traumatologist", new ArrayList<>()),
                new Doctor("MagamedzakirSaharov326", "9V3JaO1Ollde",
                        "Магамедзакир", "Сахаров", "Анатольевич", "124", "Dentist", new ArrayList<>()));

        for (Doctor d : doctors) {
            insertUser(d);
        }

        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        LocalDate commissionDate = LocalDate.of(2020, 4, 7);
        LocalTime commissionTime = LocalTime.of(14, 30);
        List<Integer> doctorIds = doctors.stream().map(User::getId).collect(Collectors.toList());
        String ticketTitle = TicketFactory.buildTicketToCommission(commissionDate, commissionTime, doctorIds);
        String cabinet = doctorCreatedCommission.getCabinet();
        int patientId = patient.getId();
        int duration = 120;

        TicketToMedicalCommission ticket =
                new TicketToMedicalCommission(ticketTitle, cabinet, commissionDate, commissionTime, patientId, doctorIds, duration);
        createMedicalCommission(ticket);
        denyMedicalCommission(ticketTitle);
    }

    @Test
    public void getEmptyListTicketsToMedicalCommissionTest() {
        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        getTicketsToMedicalCommission(patient.getId(), new ArrayList<>());
    }

    @Test
    public void getTicketsToMedicalCommissionTest() {
        Map<String, Doctor> doctors = Stream.of(
                new Doctor("MenaAltyrev568", "ceVCCj14uw4H",
                        "Варсеник", "Чудина", "Ивановна", "342", "Therapist", new ArrayList<>()),
                new Doctor("ArnestinaChehova823", "l1E57ydLV9M1",
                        "Арнестина", "Чехова", "Ефимовна", "104", "Dentist", new ArrayList<>()),
                new Doctor("KostantinVolochkov999", "d8Nb22tloSMQ",
                        "Костантин", "Ильич", null, "205", "Surgeon", new ArrayList<>()),
                new Doctor("AzhinaSolntseva451", "2I7S5a7ze911",
                        "Ажина", "Солнцева", "Олеговна", "306", "Therapist", new ArrayList<>()),
                new Doctor("BalmuradOblomov794", "TwQXwwyAJy64",
                        "Балмурад", "Обломов", "Андреевич", "471", "Traumatologist", new ArrayList<>()),
                new Doctor("MagamedzakirSaharov326", "9V3JaO1Ollde",
                        "Магамедзакир", "Сахаров", "Анатольевич", "124", "Dentist", new ArrayList<>()))
                .collect(Collectors.toMap(User::getLogin, d -> d));

        for (Doctor d : doctors.values()) {
            insertUser(d);
        }

        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        // Create some commissions

        Doctor doctorCreatedCommission = doctors.get("MagamedzakirSaharov326");
        LocalDate commissionDate = LocalDate.of(2020, 4, 7);
        LocalTime commissionTime = LocalTime.of(14, 30);
        List<Integer> doctorIds = doctors.values().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        String ticketTitle = TicketFactory.buildTicketToCommission(commissionDate, commissionTime, doctorIds);
        String cabinet = doctorCreatedCommission.getCabinet();
        int patientId = patient.getId();
        int duration = 120;

        List<TicketToMedicalCommission> expectedTickets = new ArrayList<>();
        expectedTickets.add(new TicketToMedicalCommission(ticketTitle, cabinet, commissionDate, commissionTime, patientId, doctorIds, duration));

        doctorCreatedCommission = doctors.get("AzhinaSolntseva451");
        commissionDate = LocalDate.of(2020, 3, 25);
        commissionTime = LocalTime.of(13, 0);
        doctorIds = Stream.of(doctors.get("MenaAltyrev568"), doctors.get("ArnestinaChehova823"), doctorCreatedCommission)
                .map(User::getId)
                .collect(Collectors.toList());
        ticketTitle = TicketFactory.buildTicketToCommission(commissionDate, commissionTime, doctorIds);
        cabinet = doctors.get("MenaAltyrev568").getCabinet();
        duration = 30;
        expectedTickets.add(new TicketToMedicalCommission(ticketTitle, cabinet, commissionDate, commissionTime, patientId, doctorIds, duration));

        doctorCreatedCommission = doctors.get("BalmuradOblomov794");
        commissionDate = LocalDate.of(2020, 5, 10);
        commissionTime = LocalTime.of(8, 0);
        doctorIds = Stream.of(doctors.get("AzhinaSolntseva451"), doctorCreatedCommission)
                .map(User::getId)
                .collect(Collectors.toList());
        ticketTitle = TicketFactory.buildTicketToCommission(commissionDate, commissionTime, doctorIds);
        cabinet = doctorCreatedCommission.getCabinet();
        duration = 100;
        expectedTickets.add(new TicketToMedicalCommission(ticketTitle, cabinet, commissionDate, commissionTime, patientId, doctorIds, duration));

        for (TicketToMedicalCommission t : expectedTickets) {
            createMedicalCommission(t);
        }

        getTicketsToMedicalCommission(patientId, expectedTickets);
    }

    @Test(expected = RuntimeException.class)
    public void insertTicketToMedicalCommissionWithNullTitleTest() {
        Doctor doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", new ArrayList<>());
        insertUser(doctor);

        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        TicketToMedicalCommission incorrectTicket =
                new TicketToMedicalCommission(null, doctor.getCabinet(),
                        LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), patient.getId(),
                        Collections.singletonList(doctor.getId()), 120);
        createMedicalCommission(incorrectTicket);
    }

    @Test(expected = RuntimeException.class)
    public void insertTicketToMedicalCommissionWithIncorrectCabinetTest() {
        Doctor doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", new ArrayList<>());
        insertUser(doctor);

        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        TicketToMedicalCommission incorrectTicket =
                new TicketToMedicalCommission("Ticket title", "Incorrect cabinet",
                        LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), patient.getId(),
                        Collections.singletonList(doctor.getId()), 120);
        createMedicalCommission(incorrectTicket);
    }

    @Test(expected = RuntimeException.class)
    public void insertTicketToMedicalCommissionWithNullCabinetTest() {
        Doctor doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", new ArrayList<>());
        insertUser(doctor);

        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        TicketToMedicalCommission incorrectTicket =
                new TicketToMedicalCommission("Ticket title", null,
                        LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), patient.getId(),
                        Collections.singletonList(doctor.getId()), 120);
        createMedicalCommission(incorrectTicket);
    }

    @Test(expected = RuntimeException.class)
    public void insertTicketToMedicalCommissionWithNullDateTest() {
        Doctor doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", new ArrayList<>());
        insertUser(doctor);

        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        TicketToMedicalCommission incorrectTicket =
                new TicketToMedicalCommission("Ticket title", doctor.getCabinet(),
                        null, LocalTime.of(8, 0), patient.getId(),
                        Collections.singletonList(doctor.getId()), 120);
        createMedicalCommission(incorrectTicket);
    }

    @Test(expected = RuntimeException.class)
    public void insertTicketToMedicalCommissionWithNullTimeTest() {
        Doctor doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", new ArrayList<>());
        insertUser(doctor);

        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        TicketToMedicalCommission incorrectTicket =
                new TicketToMedicalCommission("Ticket title", doctor.getCabinet(),
                        LocalDate.of(2020, 3, 4), null, patient.getId(),
                        Collections.singletonList(doctor.getId()), 120);
        createMedicalCommission(incorrectTicket);
    }

    @Test(expected = RuntimeException.class)
    public void insertTicketToMedicalCommissionWithIncorrectPatientIdTest() {
        Doctor doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", new ArrayList<>());
        insertUser(doctor);

        TicketToMedicalCommission incorrectTicket =
                new TicketToMedicalCommission("Ticket title", doctor.getCabinet(),
                        LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), -1,
                        Collections.singletonList(doctor.getId()), 120);
        createMedicalCommission(incorrectTicket);
    }

    @Test(expected = RuntimeException.class)
    public void insertTicketToMedicalCommissionWithEmptyDoctorIdsTest() {
        Doctor doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", new ArrayList<>());
        insertUser(doctor);

        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        TicketToMedicalCommission incorrectTicket =
                new TicketToMedicalCommission("Ticket title", doctor.getCabinet(),
                        LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), patient.getId(),
                        new ArrayList<>(), 120);
        createMedicalCommission(incorrectTicket);
    }

    @Test(expected = RuntimeException.class)
    public void insertTicketToMedicalCommissionWithIncorrectDoctorIdsTest() {
        Doctor doctor = new Doctor("AbilfatGolovin602", "FW9E1x2m2u18",
                "Абилфат", "Головин", "Анатольевич", "261", "Dentist", new ArrayList<>());
        insertUser(doctor);

        Patient patient = new Patient("BogertZorin407", "saTPqK1Hs8gu",
                "Богерт", "Зорин", null, "5vwvd7@amazingmail.xyz",
                "457441, г. Орел, ул. Вешних Вод, дом 26, квартира 398", "+79465104246");
        insertUser(patient);

        TicketToMedicalCommission incorrectTicket =
                new TicketToMedicalCommission("Ticket title", doctor.getCabinet(),
                        LocalDate.of(2020, 3, 4), LocalTime.of(8, 0), patient.getId(),
                        Arrays.asList(-1, -2), 120);
        createMedicalCommission(incorrectTicket);
    }
}