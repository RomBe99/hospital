package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.util.ScheduleGenerators;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class UserDaoTest extends DaoTestApi {
    @Test
    public void getDoctorInformationWithFullScheduleTest() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", new ArrayList<>());
        insertUser(doctor);

        int duration = 30;
        int durationPerDay = 5;
        int doctorId = doctor.getId();
        List<Integer> weekDays = Arrays.asList(1, 2, 3);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(12, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);
        doctor.setSchedule(schedule);

        int patientId = 0;
        Doctor actualDoctor = getDoctorInformationWithSchedule(patientId, doctorId, null, null);
        actualDoctor.setLogin(doctor.getLogin());
        actualDoctor.setPassword(doctor.getPassword());

        Assert.assertEquals(doctor, actualDoctor);
    }

    @Test
    public void getDoctorInformationWithFullScheduleAndAppointmentsTest() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", new ArrayList<>());
        insertUser(doctor);

        int duration = 30;
        int durationPerDay = 5;
        int doctorId = doctor.getId();
        List<Integer> weekDays = Arrays.asList(1, 2, 3);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(12, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators.generateDtoRequestWithWeekSchedule(duration,
                dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        Patient patient1 = new Patient("EallaMerkulova844", "2PrKexqqZ5FK",
                "Еалла", "Меркулова", null, "jojece9022@aenmail.net",
                "403105, г. Красной Чикой, ул. Сыромятнический 1-й пер, дом 182, квартира 729", "+79835018633");
        insertUser(patient1);

        List<String> patient1Tickets = Arrays.asList(
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 4), LocalTime.of(12, 0)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 23), LocalTime.of(13, 0)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 24), LocalTime.of(12, 30)));
        patient1Tickets.forEach(ticketTitle -> appointmentToDoctor(patient1.getId(), ticketTitle));

        Patient patient2 = new Patient("MarikaLevchenko0", "nrusOqHiqsbB",
                "Марика", "Левченко", null, "hzdvmndhtcuraxvchh@ttirv.org",
                "391634, г. Емельяново, ул. Полевой (Усть-Славянка) пер, дом 5, квартира 44", "89340237045");
        insertUser(patient2);

        List<String> patient2Tickets = Arrays.asList(
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 4), LocalTime.of(12, 30)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 16), LocalTime.of(12, 0)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 24), LocalTime.of(14, 0)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 30), LocalTime.of(13, 0)),
                TicketFactory.buildTicketToDoctor(doctorId, LocalDate.of(2020, 3, 31), LocalTime.of(14, 0)));
        patient2Tickets.forEach(ticketTitle -> appointmentToDoctor(patient2.getId(), ticketTitle));

        int patientId = patient1.getId();
        Doctor actualDoctor = getDoctorInformationWithSchedule(patientId, doctorId, null, null);
        actualDoctor.setLogin(doctor.getLogin());
        actualDoctor.setPassword(doctor.getPassword());

        List<TimeCell> temp = new LinkedList<>();
        actualDoctor.getSchedule().forEach(sc -> temp.addAll(sc.getCells()));

        Map<String, TimeCell> allTimeCells = temp.stream().collect(Collectors.toMap(TimeCell::getTitle, tc -> tc));
        patient2Tickets.forEach(ticketTitle -> Assert.assertFalse(allTimeCells.containsKey(ticketTitle)));

        patient1.setLogin(null);
        patient1.setPassword(null);
        patient1Tickets.forEach(ticketTitle -> Assert.assertEquals(allTimeCells.get(ticketTitle).getPatient(), patient1));

        doctor.setSchedule(actualDoctor.getSchedule());
        Assert.assertEquals(doctor, actualDoctor);
    }

    @Test
    public void getDoctorInformationWithNonFullScheduleTest() {
        Doctor doctor = new Doctor("IvstaliyaMaksimova821", "VMWr9LVh5dVI",
                "Ивсталия", "Валерьевна", "Максимова", "205", "Surgeon", new ArrayList<>());
        insertUser(doctor);

        int duration = 30;
        int durationPerDay = 5;
        int doctorId = doctor.getId();
        List<Integer> weekDays = Arrays.asList(1, 2, 3);
        LocalDate dateStart = LocalDate.of(2020, 3, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        LocalTime timeStart = LocalTime.of(12, 0);
        LocalTime timeEnd = timeStart.plusMinutes(duration * durationPerDay);

        DtoRequestWithSchedule requestWithSchedule = ScheduleGenerators
                .generateDtoRequestWithWeekSchedule(duration, dateStart, dateEnd, timeStart, timeEnd, weekDays);

        List<ScheduleCell> schedule = ScheduleGenerators.generateSchedule(doctorId, requestWithSchedule);
        insertSchedule(doctorId, schedule);

        LocalDate bottomBound = dateStart.plusDays(5);
        LocalDate upperBound = dateEnd.minusDays(10);
        schedule = schedule.stream()
                .filter(sc -> upperBound.isAfter(sc.getDate()) && bottomBound.isBefore(sc.getDate()))
                .collect(Collectors.toList());
        doctor.setSchedule(schedule);

        int patientId = 0;
        Doctor actualDoctor = getDoctorInformationWithSchedule(patientId, doctorId, bottomBound, upperBound);
        actualDoctor.setLogin(doctor.getLogin());
        actualDoctor.setPassword(doctor.getPassword());

        Assert.assertEquals(doctor, actualDoctor);
    }
}