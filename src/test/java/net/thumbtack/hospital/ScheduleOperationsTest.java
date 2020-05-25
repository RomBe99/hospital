package net.thumbtack.hospital;

import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.ScheduleCell;
import net.thumbtack.hospital.model.TimeCell;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleOperationsTest extends BaseTest {
    public static List<ScheduleCell> generateSchedule(List<Integer> durations, int daysCount, LocalDate startDate, Doctor doctor) {
        LocalTime startTime = LocalTime.now().withNano(0);
        List<TimeCell> timeCells = new ArrayList<>(durations.size());

        for (int d : durations) {
            timeCells.add(new TimeCell(startTime, d));
            startTime = startTime.plusMinutes(d);
        }

        List<ScheduleCell> schedule = new ArrayList<>(daysCount);

        for (int i = 0; i < daysCount; i++) {
            schedule.add(new ScheduleCell(0, doctor, startDate, timeCells));
            startDate = startDate.plusDays(1);
        }

        return schedule;
    }

    @Test
    public void insertNewScheduleTest1() {
        Doctor expectedDoctor = insertDoctor("FrolovaBahiya454", "10AFVcPBcmbY",
                "Бахия", "Фролова", "Геннадиевна",
                "306", "Surgeon");

        List<Integer> durations = Arrays.asList(15, 30, 45);
        LocalDate startDate = LocalDate.now();
        int daysCount = 5;
        List<ScheduleCell> schedule = generateSchedule(durations, daysCount, startDate, expectedDoctor);

        insertSchedule(schedule, expectedDoctor.getId());

        List<ScheduleCell> actualSchedule = userDao
                .getDoctorInformation(0, expectedDoctor.getId(), startDate, startDate.plusDays(daysCount))
                .getSchedule();

        for (int i = 0; i < daysCount; i++) {
            Assert.assertEquals(schedule.get(i).getId(), actualSchedule.get(i).getId());
            Assert.assertEquals(schedule.get(i).getDate(), actualSchedule.get(i).getDate());

            for (int j = 0; j < durations.size(); j++) {
                Assert.assertEquals(schedule.get(i).getCells().get(j).getTime(), actualSchedule.get(i).getCells().get(j).getTime());
                Assert.assertEquals(schedule.get(i).getCells().get(j).getDuration(), actualSchedule.get(i).getCells().get(j).getDuration());
            }
        }
    }

    @Test
    public void insertNewScheduleTest2() {
        Doctor expectedDoctor = insertDoctor("FrolovaBahiya454", "10AFVcPBcmbY",
                "Бахия", "Фролова", "Геннадиевна",
                "306", "Surgeon");

        List<Integer> durations = Arrays.asList(15, 30, 45);
        LocalDate startDate = LocalDate.now();
        int daysCount = 5;
        List<ScheduleCell> schedule = generateSchedule(durations, daysCount, startDate, expectedDoctor);

        insertSchedule(schedule, expectedDoctor.getId());

        List<ScheduleCell> actualSchedule = userDao
                .getDoctorInformation(0, expectedDoctor.getId(), null, null)
                .getSchedule();

        for (int i = 0; i < daysCount; i++) {
            Assert.assertEquals(schedule.get(i).getId(), actualSchedule.get(i).getId());
            Assert.assertEquals(schedule.get(i).getDate(), actualSchedule.get(i).getDate());

            for (int j = 0; j < durations.size(); j++) {
                Assert.assertEquals(schedule.get(i).getCells().get(j).getTime(), actualSchedule.get(i).getCells().get(j).getTime());
                Assert.assertEquals(schedule.get(i).getCells().get(j).getDuration(), actualSchedule.get(i).getCells().get(j).getDuration());
            }
        }
    }

    @Test
    public void insertNewScheduleTest3() {
        Doctor expectedDoctor = insertDoctor("FrolovaBahiya454", "10AFVcPBcmbY",
                "Бахия", "Фролова", "Геннадиевна",
                "306", "Surgeon");

        List<Integer> durations = Arrays.asList(15, 30, 45);
        LocalDate startDate = LocalDate.now();
        int daysCount = 5;
        List<ScheduleCell> schedule = generateSchedule(durations, daysCount, startDate, expectedDoctor);

        insertSchedule(schedule, expectedDoctor.getId());

        List<ScheduleCell> actualSchedule = userDao
                .getDoctorInformation(0, expectedDoctor.getId(), startDate, null)
                .getSchedule();

        for (int i = 0; i < daysCount; i++) {
            Assert.assertEquals(schedule.get(i).getId(), actualSchedule.get(i).getId());
            Assert.assertEquals(schedule.get(i).getDate(), actualSchedule.get(i).getDate());

            for (int j = 0; j < durations.size(); j++) {
                Assert.assertEquals(schedule.get(i).getCells().get(j).getTime(), actualSchedule.get(i).getCells().get(j).getTime());
                Assert.assertEquals(schedule.get(i).getCells().get(j).getDuration(), actualSchedule.get(i).getCells().get(j).getDuration());
            }
        }
    }

    @Test
    public void insertNewScheduleTest4() {
        Doctor expectedDoctor = insertDoctor("FrolovaBahiya454", "10AFVcPBcmbY",
                "Бахия", "Фролова", "Геннадиевна",
                "306", "Surgeon");

        List<Integer> durations = Arrays.asList(15, 30, 45);
        LocalDate startDate = LocalDate.now();
        int daysCount = 5;
        int tmpDays = 2;
        List<ScheduleCell> schedule = generateSchedule(durations, daysCount, startDate, expectedDoctor);

        insertSchedule(schedule, expectedDoctor.getId());

        List<ScheduleCell> actualSchedule = userDao
                .getDoctorInformation(0, expectedDoctor.getId(), startDate, startDate.plusDays(tmpDays))
                .getSchedule();

        for (int i = 0; i < tmpDays; i++) {
            Assert.assertEquals(schedule.get(i).getId(), actualSchedule.get(i).getId());
            Assert.assertEquals(schedule.get(i).getDate(), actualSchedule.get(i).getDate());

            for (int j = 0; j < durations.size(); j++) {
                Assert.assertEquals(schedule.get(i).getCells().get(j).getTime(), actualSchedule.get(i).getCells().get(j).getTime());
                Assert.assertEquals(schedule.get(i).getCells().get(j).getDuration(), actualSchedule.get(i).getCells().get(j).getDuration());
            }
        }
    }

    @Test
    public void getEmptyScheduleTest() {
        Doctor expectedDoctor = insertDoctor("FrolovaBahiya454", "10AFVcPBcmbY",
                "Бахия", "Фролова", "Геннадиевна",
                "306", "Surgeon");

        List<Integer> durations = Arrays.asList(15, 30, 45);
        LocalDate startDate = LocalDate.now();
        int daysCount = 5;

        Doctor actualDoctor = userDao.getDoctorInformation(0, expectedDoctor.getId(), startDate, null);

        Assert.assertNull(actualDoctor);
    }
}