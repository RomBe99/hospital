package net.thumbtack.hospital;

import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoctorOperationsTest extends BaseTest {
    @Test
    public void insertDoctorTest() {
        String login = "FrolovaBahiya454";
        String password = "10AFVcPBcmbY";
        String firstName = "Бахия";
        String lastName = "Фролова";
        String patronymic = "Геннадиевна";
        String specialty = "Surgeon";
        String cabinet = "306";

        Doctor expectedDoctor = new Doctor(login, password, firstName, lastName, patronymic, cabinet, specialty, new ArrayList<>());

        try {
            Doctor insertedDoctor = insertDoctor(login, password, firstName, lastName, patronymic, cabinet, specialty);
            expectedDoctor.setId(insertedDoctor.getId());

            Assert.assertEquals(expectedDoctor, insertedDoctor);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void removeDoctorTest() {
        String login = "FrolovaBahiya454";
        String password = "10AFVcPBcmbY";
        String firstName = "Бахия";
        String lastName = "Фролова";
        String patronymic = "Геннадиевна";
        String specialty = "Surgeon";
        String cabinet = "306";

        Doctor expectedDoctor = new Doctor(login, password, firstName, lastName, patronymic, cabinet, specialty, new ArrayList<>());

        try {
            Doctor insertedDoctor = insertDoctor(login, password, firstName, lastName, patronymic, cabinet, specialty);
            expectedDoctor.setId(insertedDoctor.getId());

            Assert.assertEquals(expectedDoctor, insertedDoctor);

            doctorDao.removeDoctor(expectedDoctor.getId());

            Doctor removedDoctor = doctorDao.getDoctorById(expectedDoctor.getId());
            Assert.assertNull(removedDoctor);
        } catch (RuntimeException ex) {
            Assert.fail();
        }
    }

    @Test
    public void getDoctorsWithSchedules1() {
        List<Integer> durations = Arrays.asList(15, 30, 45);
        int daysCount = 2;
        LocalDate startDate = LocalDate.now();

        Doctor doctor1 = insertDoctor("GogolZulfiya56", "hJBcK9QR5nio",
                "Зульфия", "Гоголь", "Алексеевна",
                "104", "Dentist");
        List<ScheduleCell> sc1 = insertSchedule(ScheduleOperationsTest.generateSchedule(durations, daysCount, startDate, doctor1), doctor1.getId());
        doctor1.setSchedule(sc1);

        Doctor doctor2 = insertDoctor("DobronravovArbis265", "byz3H8W1I0ZE",
                "Арбис", "Добронравов", "Юрьевич",
                "205", "Surgeon");
        List<ScheduleCell> sc2 = insertSchedule(ScheduleOperationsTest.generateSchedule(durations, daysCount, startDate, doctor2), doctor2.getId());
        doctor2.setSchedule(sc2);

        Doctor doctor3 = insertDoctor("SobolAlahverdi246", "B6CIXQhaH54N",
                "Алахверди", "Соболь", "Артемович",
                "306", "Traumatologist");
        List<ScheduleCell> sc3 = insertSchedule(ScheduleOperationsTest.generateSchedule(durations, daysCount, startDate, doctor3), doctor3.getId());
        doctor3.setSchedule(sc3);

        Patient patient = insertPatient("MischenkoRobertino218", "rZynw4IVpDem",
                "Робертино", "Мищенко", "Аркадьевич", "xikox93966@ximtyl.com",
                "420074, г. Аликово, ул. Татарский Б. пер, дом 57, квартира 287", "+79264876938");

        LocalDate ticketDate = startDate.plusDays(1);
        LocalTime ticketTime = sc2.get(1).getCells().get(1).getTime();
        patientDao.appointmentToDoctor(patient.getId(), doctor2.getId(), ticketDate, ticketTime);

        List<Doctor> actualDoctors = userDao.getDoctorsInformation(patient.getId(), null, startDate, startDate.plusDays(daysCount));
        actualDoctors.sort(Comparator.comparingInt(Doctor::getId));

        List<Doctor> expectedDoctors = Arrays.asList(doctor1, doctor2, doctor3);
        expectedDoctors.sort(Comparator.comparingInt(Doctor::getId));

        Assert.assertEquals(expectedDoctors.size(), actualDoctors.size());

        for (int i = 0; i < actualDoctors.size(); i++) {
            Assert.assertEquals(actualDoctors.get(i).getId(), expectedDoctors.get(i).getId());
        }

        TimeCell timeCellWithPatient = actualDoctors.stream()
                .filter(d -> d.getId() == doctor2.getId())
                .findFirst().get()
                .getSchedule().stream()
                .filter(sc -> ticketDate.equals(sc.getDate()))
                .findFirst().get()
                .getCells().stream()
                .filter(tc -> ticketTime.equals(tc.getTime()))
                .findFirst().get();

        Assert.assertEquals(patient.getId(), timeCellWithPatient.getPatient().getId());
        Assert.assertEquals(ticketTime, timeCellWithPatient.getTime());
    }

    @Test
    public void getDoctorsWithSchedules2() {
        List<Integer> durations = Arrays.asList(15, 30, 45);
        int daysCount = 2;
        String speciality = "Surgeon";
        LocalDate startDate = LocalDate.now();

        Doctor doctor1 = insertDoctor("GogolZulfiya56", "hJBcK9QR5nio",
                "Зульфия", "Гоголь", "Алексеевна",
                "104", speciality);
        List<ScheduleCell> sc1 = insertSchedule(ScheduleOperationsTest.generateSchedule(durations, daysCount, startDate, doctor1), doctor1.getId());
        doctor1.setSchedule(sc1);

        Doctor doctor2 = insertDoctor("DobronravovArbis265", "byz3H8W1I0ZE",
                "Арбис", "Добронравов", "Юрьевич",
                "205", speciality);
        List<ScheduleCell> sc2 = insertSchedule(ScheduleOperationsTest.generateSchedule(durations, daysCount, startDate, doctor2), doctor2.getId());
        doctor2.setSchedule(sc2);

        Doctor doctor3 = insertDoctor("SobolAlahverdi246", "B6CIXQhaH54N",
                "Алахверди", "Соболь", "Артемович",
                "306", "Traumatologist");
        List<ScheduleCell> sc3 = insertSchedule(ScheduleOperationsTest.generateSchedule(durations, daysCount, startDate, doctor3), doctor3.getId());
        doctor3.setSchedule(sc3);

        List<Doctor> actualDoctors = userDao.getDoctorsInformation(0, speciality, null, null);
        actualDoctors.sort(Comparator.comparingInt(Doctor::getId));

        List<Doctor> expectedDoctors = Stream.of(doctor1, doctor2, doctor3)
                .filter(d -> d.getSpecialty().equals(speciality))
                .sorted(Comparator.comparingInt(Doctor::getId))
                .collect(Collectors.toList());

        Assert.assertEquals(expectedDoctors.size(), actualDoctors.size());

        for (int i = 0; i < actualDoctors.size(); i++) {
            Assert.assertEquals(actualDoctors.get(i).getId(), expectedDoctors.get(i).getId());
            Assert.assertEquals(speciality, expectedDoctors.get(i).getSpecialty());
        }
    }

    @Test
    public void getEmptyDoctorsList() {
        List<Doctor> emptyDoctorsList = doctorDao.getAllDoctors();

        Assert.assertTrue(emptyDoctorsList.isEmpty());
    }
}