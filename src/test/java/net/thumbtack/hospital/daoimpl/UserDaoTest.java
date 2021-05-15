package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.util.ScheduleGenerators;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDaoTest extends DaoTestApi {
    @Test
    public void getDoctorInformationWithFullScheduleTest() {
        final var doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        final var duration = 30;
        final var durationPerDay = 5;
        final var doctorId = doctor.getId();
        final var weekDays = List.of(1, 2, 3);
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = dateStart.plusMonths(1);
        final var timeStart = LocalTime.of(12, 0);
        final var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        final var requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final var schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);
        doctor.setSchedule(schedule);

        final var patientId = 0;
        final var actualDoctor = getDoctorInformationWithSchedule(patientId, doctorId, null, null);
        actualDoctor.setLogin(doctor.getLogin());
        actualDoctor.setPassword(doctor.getPassword());

        Assertions.assertEquals(doctor, actualDoctor);
    }

    @Test
    public void getDoctorInformationWithFullScheduleAndAppointmentsTest() {
        final var doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        final var duration = 30;
        final var durationPerDay = 5;
        final var doctorId = doctor.getId();
        final var weekDays = List.of(1, 2, 3);
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = dateStart.plusMonths(1);
        final var timeStart = LocalTime.of(12, 0);
        final var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        final var requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final var schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        final var patient1 = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", null, "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient1);

        final var patient1Tickets = List.of(
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 4), LocalTime.of(12, 0)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 23), LocalTime.of(13, 0)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 24), LocalTime.of(12, 30)));
        patient1Tickets.forEach(ticketTitle -> appointmentToDoctor(patient1.getId(), ticketTitle));

        final var patient2 = new Patient("MarikaLevchenko0", "nrusOqHiqsbB",
                "Марика", "Левченко", null, "hzdvmndhtcuraxvchh@ttirv.org",
                "391634, г. Емельяново, ул. Полевой (Усть-Славянка) пер, дом 5, квартира 44", "89340237045");
        insertUser(patient2);

        final var patient2Tickets = List.of(
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 4), LocalTime.of(12, 30)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 16), LocalTime.of(12, 0)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 24), LocalTime.of(14, 0)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 30), LocalTime.of(13, 0)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 31), LocalTime.of(14, 0)));
        patient2Tickets.forEach(ticketTitle -> appointmentToDoctor(patient2.getId(), ticketTitle));

        final var patientId = patient1.getId();
        final var actualDoctor = getDoctorInformationWithSchedule(patientId, doctorId, null, null);
        actualDoctor.setLogin(doctor.getLogin());
        actualDoctor.setPassword(doctor.getPassword());

        final var temp = new LinkedList<TimeCell>();
        actualDoctor.getSchedule().forEach(sc -> temp.addAll(sc.getCells()));

        final var allTimeCells = temp.stream().collect(Collectors.toMap(TimeCell::getTitle, tc -> tc));
        patient2Tickets.forEach(ticketTitle -> Assertions.assertFalse(allTimeCells.containsKey(ticketTitle)));

        patient1.setLogin(null);
        patient1.setPassword(null);
        patient1Tickets.forEach(ticketTitle -> Assertions.assertEquals(allTimeCells.get(ticketTitle).getPatient(), patient1));

        doctor.setSchedule(actualDoctor.getSchedule());
        Assertions.assertEquals(doctor, actualDoctor);
    }

    @Test
    public void getDoctorInformationWithNonFullScheduleTest() {
        final var doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        final var duration = 30;
        final var durationPerDay = 5;
        final var doctorId = doctor.getId();
        final var weekDays = List.of(1, 2, 3);
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = dateStart.plusMonths(1);
        final var timeStart = LocalTime.of(12, 0);
        final var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        final var requestWithSchedule = ScheduleGenerators
                .generateDtoRequestWithWeekSchedule(duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        var schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        final var bottomBound = dateStart.plusDays(5);
        final var upperBound = dateEnd.minusDays(10);
        schedule = schedule.stream()
                .filter(sc -> upperBound.isAfter(sc.getDate()) && bottomBound.isBefore(sc.getDate()))
                .collect(Collectors.toList());
        doctor.setSchedule(schedule);

        final var patientId = 0;
        final var actualDoctor = getDoctorInformationWithSchedule(patientId, doctorId, bottomBound, upperBound);
        actualDoctor.setLogin(doctor.getLogin());
        actualDoctor.setPassword(doctor.getPassword());

        Assertions.assertEquals(doctor, actualDoctor);
    }
}