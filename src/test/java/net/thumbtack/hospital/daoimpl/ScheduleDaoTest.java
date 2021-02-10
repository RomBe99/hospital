package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.model.ticket.Ticket;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.util.ScheduleGenerators;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ScheduleDaoTest extends DaoTestApi {
    @Test
    public void insertScheduleTest() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        int duration = 30;
        int durationPerDay = 5;
        int doctorId = doctor.getId();
        List<Integer> weekDays = Arrays.asList(1, 2, 3, 4, 5);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(12, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);
    }

    @Test(expected = RuntimeException.class)
    public void tryInsertScheduleForIncorrectDoctorId() {
        int duration = 30;
        int durationPerDay = 5;
        int incorrectDoctorId = -2;
        List<Integer> weekDays = Arrays.asList(1, 2, 3, 4, 5);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(12, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(incorrectDoctorId, requestWithSchedule);
        insertSchedule(incorrectDoctorId, schedule);
    }

    @Test(expected = RuntimeException.class)
    public void tryInsertEmptyScheduleTest() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        insertSchedule(doctor.getId(), Collections.emptyList());
    }

    @Test
    public void editScheduleTest1() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        int doctorId = doctor.getId();
        int duration = 10;
        int durationPerDay = 20;
        List<Integer> weekDays = Arrays.asList(1, 3);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(8, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        // Editing schedule test

        duration = 15;
        durationPerDay = 18;
        weekDays = Arrays.asList(1, 4, 5);
        dateStart = dateStart.plusDays(5);
        timeStart = LocalTime.of(9, 30);
        timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> newSchedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        editSchedule(doctorId, dateStart, dateEnd, newSchedule);
    }

    @Test
    public void editScheduleTest2() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        int doctorId = doctor.getId();
        int duration = 10;
        int durationPerDay = 20;
        List<Integer> weekDays = Arrays.asList(1, 3);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(8, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        // Editing schedule test

        duration = 15;
        durationPerDay = 18;
        weekDays = Arrays.asList(1, 4, 5);
        dateStart = dateStart.plusDays(5);
        dateEnd = dateEnd.minusDays(10);
        timeStart = LocalTime.of(9, 30);
        timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> newSchedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        editSchedule(doctorId, dateStart, dateEnd, newSchedule);
    }

    @Test
    public void editScheduleTest3() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        int doctorId = doctor.getId();
        int duration = 10;
        int durationPerDay = 20;
        List<Integer> weekDays = Arrays.asList(1, 3);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(8, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        // Editing schedule test

        duration = 15;
        durationPerDay = 15;
        weekDays = Arrays.asList(4, 5);
        timeStart = LocalTime.of(9, 30);
        timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> newSchedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        editSchedule(doctorId, dateStart, dateEnd, newSchedule);
    }

    @Test
    public void appointmentToDoctorTest() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        int doctorId = doctor.getId();
        int duration = 10;
        int durationPerDay = 20;
        List<Integer> weekDays = Arrays.asList(1, 3);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(8, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        Patient patient = new Patient("LivovHristofor214", "jhVvj8dk7y3s",
                "Христофор", "Львов", null, "kwumcftdtfyvyal@novaemail.com",
                "г.Панино, ул. Театральная, дом 26, квартира 230", "86405477438");
        insertUser(patient);

        List<TimeCell> cells = schedule.get((int) (Math.random() * schedule.size())).getCells();
        String ticketTitle = cells.get((int) (Math.random() * cells.size())).getTitle();
        appointmentToDoctor(patient.getId(), ticketTitle);
    }

    @Test
    public void denyTicketToDoctorTest() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        int doctorId = doctor.getId();
        int duration = 10;
        int durationPerDay = 20;
        List<Integer> weekDays = Arrays.asList(1, 3);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(8, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        Patient patient = new Patient("LivovHristofor214", "jhVvj8dk7y3s",
                "Христофор", "Львов", null, "kwumcftdtfyvyal@novaemail.com",
                "г.Панино, ул. Театральная, дом 26, квартира 230", "86405477438");
        insertUser(patient);

        List<TimeCell> cells = schedule.get((int) (Math.random() * schedule.size())).getCells();
        String ticketTitle = cells.get((int) (Math.random() * cells.size())).getTitle();
        appointmentToDoctor(patient.getId(), ticketTitle);
        int patientId = patient.getId();

        appointmentToDoctor(patientId, ticketTitle);

        denyTicket(patientId, ticketTitle);
    }

    @Test
    public void getTicketsToDoctorTest() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", Collections.emptyList());
        insertUser(doctor);

        int doctorId = doctor.getId();
        int duration = 10;
        int durationPerDay = 20;
        List<Integer> weekDays = Arrays.asList(1, 3);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(8, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        Patient patient = new Patient("LivovHristofor214", "jhVvj8dk7y3s",
                "Христофор", "Львов", null, "kwumcftdtfyvyal@novaemail.com",
                "г.Панино, ул. Театральная, дом 26, квартира 230", "86405477438");
        insertUser(patient);

        int patientId = patient.getId();
        Map<ScheduleCell, TimeCell> mappedSchedule = new HashMap<>();
        TimeCell tempTimeCell;

        for (ScheduleCell sc : schedule) {
            tempTimeCell = sc.getCells().get((int) (Math.random() * sc.getCells().size()));
            mappedSchedule.put(sc, tempTimeCell);
            appointmentToDoctor(patientId, tempTimeCell.getTitle());
        }

        List<TicketToDoctor> expectedTicketsToDoctor = new ArrayList<>();

        for (ScheduleCell sc : mappedSchedule.keySet()) {
            tempTimeCell = mappedSchedule.get(sc);
            expectedTicketsToDoctor.add(
                    new TicketToDoctor(tempTimeCell.getTitle(), doctor.getCabinet(), sc.getDate(), tempTimeCell.getTime(),
                            doctorId, doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(),
                            doctor.getSpecialty()));
        }
        expectedTicketsToDoctor.sort(Comparator.comparing(Ticket::getTitle));

        List<TicketToDoctor> actualTicketsToDoctor = getTicketsToDoctor(patientId);
        Assert.assertEquals(expectedTicketsToDoctor, actualTicketsToDoctor);
    }

    @Test
    public void getEmptyTicketsListToDoctorTest() {
        Patient patient = new Patient("LivovHristofor214", "jhVvj8dk7y3s",
                "Христофор", "Львов", null, "kwumcftdtfyvyal@novaemail.com",
                "г.Панино, ул. Театральная, дом 26, квартира 230", "86405477438");
        insertUser(patient);

        List<TicketToDoctor> actualTickets = getTicketsToDoctor(patient.getId());
        Assert.assertTrue(actualTickets.isEmpty());
    }
}