package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.model.ticket.Ticket;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ScheduleDaoTest extends DaoTestApi {
    @Test
    public void insertScheduleTest() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        final var duration = 30;
        final var durationPerDay = 5;
        final var doctorId = doctor.getId();
        final var weekDays = List.of(1, 2, 3, 4, 5);
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = dateStart.plusMonths(1);
        final var timeStart = LocalTime.of(12, 0);
        final var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        final var requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final var schedule = getScheduleGenerator().generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);
    }

    @Test
    public void tryInsertScheduleForIncorrectDoctorId() {
        final var duration = 30;
        final var durationPerDay = 5;
        final var incorrectDoctorId = -2;
        final var weekDays = List.of(1, 2, 3, 4, 5);
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = dateStart.plusMonths(1);
        final var timeStart = LocalTime.of(12, 0);
        final var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        final var requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final var schedule = getScheduleGenerator().generateSchedule(incorrectDoctorId, requestWithSchedule);

        Assertions.assertThrows(RuntimeException.class, () -> insertSchedule(incorrectDoctorId, schedule));
    }

    @Test
    public void tryInsertEmptyScheduleTest() {
        final var doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        Assertions.assertThrows(RuntimeException.class, () -> insertSchedule(doctor.getId(), Collections.emptyList()));
    }

    @Test
    public void editScheduleTest1() {
        final var doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        final var doctorId = doctor.getId();
        var duration = 10;
        var durationPerDay = 20;
        var weekDays = List.of(1, 3);
        var dateStart = LocalDate.of(2020, 3, 1);
        var dateEnd = dateStart.plusMonths(1);
        var timeStart = LocalTime.of(8, 0);
        var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        var requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        var schedule = getScheduleGenerator().generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        // Editing schedule test

        duration = 15;
        durationPerDay = 18;
        weekDays = List.of(1, 4, 5);
        dateStart = dateStart.plusDays(5);
        timeStart = LocalTime.of(9, 30);
        timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        var newSchedule = getScheduleGenerator().generateSchedule(doctorId, requestWithSchedule);
        editSchedule(doctorId, dateStart, dateEnd, newSchedule);
    }

    @Test
    public void editScheduleTest2() {
        final var doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        final var doctorId = doctor.getId();
        var duration = 10;
        var durationPerDay = 20;
        var weekDays = List.of(1, 3);
        var dateStart = LocalDate.of(2020, 3, 1);
        var dateEnd = dateStart.plusMonths(1);
        var timeStart = LocalTime.of(8, 0);
        var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        var requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        var schedule = getScheduleGenerator().generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        // Editing schedule test

        duration = 15;
        durationPerDay = 18;
        weekDays = List.of(1, 4, 5);
        dateStart = dateStart.plusDays(5);
        dateEnd = dateEnd.minusDays(10);
        timeStart = LocalTime.of(9, 30);
        timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        var newSchedule = getScheduleGenerator().generateSchedule(doctorId, requestWithSchedule);
        editSchedule(doctorId, dateStart, dateEnd, newSchedule);
    }

    @Test
    public void editScheduleTest3() {
        final var doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        final var doctorId = doctor.getId();
        var duration = 10;
        var durationPerDay = 20;
        var weekDays = List.of(1, 3);
        var dateStart = LocalDate.of(2020, 3, 1);
        var dateEnd = dateStart.plusMonths(1);
        var timeStart = LocalTime.of(8, 0);
        var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        var requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        var schedule = getScheduleGenerator().generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        // Editing schedule test

        duration = 15;
        durationPerDay = 15;
        weekDays = List.of(4, 5);
        timeStart = LocalTime.of(9, 30);
        timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        var newSchedule = getScheduleGenerator().generateSchedule(doctorId, requestWithSchedule);
        editSchedule(doctorId, dateStart, dateEnd, newSchedule);
    }

    @Test
    public void appointmentToDoctorTest() {
        final var doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        final var doctorId = doctor.getId();
        final var duration = 10;
        final var durationPerDay = 20;
        final var weekDays = List.of(1, 3);
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = dateStart.plusMonths(1);
        final var timeStart = LocalTime.of(8, 0);
        final var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        final var requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final var schedule = getScheduleGenerator().generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        final var patient = new Patient("LivovHristofor214", "jhVvj8dk7y3s",
                "Христофор", "Львов", null, "kwumcftdtfyvyal@novaemail.com",
                "г.Панино, ул. Театральная, дом 26, квартира 230", "86405477438");
        insertUser(patient);

        final var cells = schedule.get((int) (Math.random() * schedule.size())).getCells();
        final var ticketTitle = cells.get((int) (Math.random() * cells.size())).getTitle();
        appointmentToDoctor(patient.getId(), ticketTitle);
    }

    @Test
    public void denyTicketToDoctorTest() {
        final var doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        final var doctorId = doctor.getId();
        final var duration = 10;
        final var durationPerDay = 20;
        final var weekDays = List.of(1, 3);
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = dateStart.plusMonths(1);
        final var timeStart = LocalTime.of(8, 0);
        final var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        final var requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final var schedule = getScheduleGenerator().generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        final var patient = new Patient("LivovHristofor214", "jhVvj8dk7y3s",
                "Христофор", "Львов", null, "kwumcftdtfyvyal@novaemail.com",
                "г.Панино, ул. Театральная, дом 26, квартира 230", "86405477438");
        insertUser(patient);

        final var cells = schedule.get((int) (Math.random() * schedule.size())).getCells();
        final var ticketTitle = cells.get((int) (Math.random() * cells.size())).getTitle();
        appointmentToDoctor(patient.getId(), ticketTitle);
        final var patientId = patient.getId();

        appointmentToDoctor(patientId, ticketTitle);

        denyTicket(patientId, ticketTitle);
    }

    @Test
    public void getTicketsToDoctorTest() {
        final var doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        final var doctorId = doctor.getId();
        final var duration = 10;
        final var durationPerDay = 20;
        final var weekDays = List.of(1, 3);
        final var dateStart = LocalDate.of(2020, 3, 1);
        final var dateEnd = dateStart.plusMonths(1);
        final var timeStart = LocalTime.of(8, 0);
        final var timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        final var requestWithSchedule = getScheduleGenerator().generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        final var schedule = getScheduleGenerator().generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        final var patient = new Patient("LivovHristofor214", "jhVvj8dk7y3s",
                "Христофор", "Львов", null, "kwumcftdtfyvyal@novaemail.com",
                "г.Панино, ул. Театральная, дом 26, квартира 230", "86405477438");
        insertUser(patient);

        final var patientId = patient.getId();
        final var mappedSchedule = new HashMap<ScheduleCell, TimeCell>();
        TimeCell tempTimeCell;

        for (var sc : schedule) {
            tempTimeCell = sc.getCells().get((int) (Math.random() * sc.getCells().size()));
            mappedSchedule.put(sc, tempTimeCell);
            appointmentToDoctor(patientId, tempTimeCell.getTitle());
        }

        final var expectedTicketsToDoctor = new ArrayList<TicketToDoctor>();

        for (var sc : mappedSchedule.keySet()) {
            tempTimeCell = mappedSchedule.get(sc);
            expectedTicketsToDoctor.add(
                    new TicketToDoctor(tempTimeCell.getTitle(), doctor.getCabinet(), sc.getDate(), tempTimeCell.getTime(),
                            doctorId, doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                            doctor.getSpecialty()));
        }
        expectedTicketsToDoctor.sort(Comparator.comparing(Ticket::getTitle));

        final var actualTicketsToDoctor = getTicketsToDoctor(patientId);
        Assertions.assertEquals(expectedTicketsToDoctor, actualTicketsToDoctor);
    }

    @Test
    public void getEmptyTicketsListToDoctorTest() {
        final var patient = new Patient("LivovHristofor214", "jhVvj8dk7y3s",
                "Христофор", "Львов", null, "kwumcftdtfyvyal@novaemail.com",
                "г.Панино, ул. Театральная, дом 26, квартира 230", "86405477438");
        insertUser(patient);

        final var actualTickets = getTicketsToDoctor(patient.getId());
        Assertions.assertTrue(actualTickets.isEmpty());
    }
}