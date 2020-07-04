package net.thumbtack.hospital;

import net.thumbtack.hospital.dao.AdminDao;
import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.hospital.daoimpl.DoctorDaoImpl;
import net.thumbtack.hospital.daoimpl.PatientDaoImpl;
import net.thumbtack.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.hospital.debug.DebugDao;
import net.thumbtack.hospital.debug.DebugDaoImpl;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class BaseTest {
    protected final AdminDao adminDao = new AdminDaoImpl();
    protected final DoctorDao doctorDao = new DoctorDaoImpl();
    protected final PatientDao patientDao = new PatientDaoImpl();
    protected final UserDao userDao = new UserDaoImpl();
    private final DebugDao debugDao = new DebugDaoImpl();

    private static boolean setUpIsDone = false;

    @BeforeClass
    public static void setUp() {
        if (!setUpIsDone) {
            boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();

            if (!initSqlSessionFactory) {
                throw new RuntimeException("Can't create connection, stop");
            }

            setUpIsDone = true;
        }
    }

    @Before
    public void clearDatabase() {
        debugDao.clearUsers();
        debugDao.clearAdministrators();
        debugDao.clearDoctors();
        debugDao.clearPatients();
        debugDao.clearCommissionDoctors();
        debugDao.clearLoggedInUsers();
        debugDao.clearScheduleCells();
        debugDao.clearTimeCells();
        debugDao.clearUsers();
        debugDao.clearMedicalCommissions();
    }

    @Test(expected = PermissionDeniedException.class)
    public void loginAndLogoutTest() throws PermissionDeniedException {
        String login = "VorobevaAltina312";
        String password = "fwZO9tIyAQlf";
        String firstName = "Альтина";
        String lastName = "Воробьёва";
        String patronymic = "Максимовна";
        String position = "Test Admin";

        Administrator expectedAdmin = new Administrator(login, password, firstName, lastName, patronymic, position);

        try {
            Administrator insertedAdmin = insertAdministrator(login, password, firstName, lastName, patronymic, position);
            expectedAdmin.setId(insertedAdmin.getId());

            Assert.assertEquals(expectedAdmin, insertedAdmin);

            String sessionId = UUID.randomUUID().toString();
            int adminIdWhenLogin = userDao.login(sessionId, expectedAdmin.getLogin(), expectedAdmin.getPassword());

            Assert.assertEquals(insertedAdmin.getId(), adminIdWhenLogin);

            int idAfterCheckingPermissions = userDao.hasPermissions(sessionId);

            Assert.assertEquals(adminIdWhenLogin, idAfterCheckingPermissions);

            userDao.logout(sessionId);
            userDao.hasPermissions(sessionId);
        } catch (RuntimeException e) {
            Assert.fail();
        }
    }

    protected Administrator insertAdministrator(String login, String password,
                                                String firstName, String lastName, String patronymic,
                                                String position) {
        Administrator administrator = new Administrator(login, password, firstName, lastName, patronymic, position);
        adminDao.insertAdministrator(administrator);

        Assert.assertNotEquals(0, administrator.getId());

        return administrator;
    }

    protected Doctor insertDoctor(String login, String password,
                                  String firstName, String lastName, String patronymic,
                                  String cabinetName, String doctorSpecialtyName) {
        Doctor doctor = new Doctor(login, password, firstName, lastName, patronymic, cabinetName, doctorSpecialtyName, new ArrayList<>());
        doctorDao.insertDoctor(doctor);

        Assert.assertNotEquals(0, doctor.getId());

        return doctor;
    }

    protected Patient insertPatient(String login, String password,
                                    String firstName, String lastName, String patronymic,
                                    String email, String address, String phone) {
        Patient patient = new Patient(login, password, firstName, lastName, patronymic, email, address, phone);
        patientDao.insertPatient(patient);

        Assert.assertNotEquals(0, patient.getId());

        return patient;
    }

    protected List<ScheduleCell> insertSchedule(List<ScheduleCell> schedule, int doctorId) {
        schedule.sort(Comparator.comparing(ScheduleCell::getDate));
        schedule.forEach(sc -> sc.getCells().sort(Comparator.comparing(TimeCell::getTime)));

        try {
            LocalDate startDate = schedule.stream().min(Comparator.comparing(ScheduleCell::getDate)).orElseThrow(Exception::new).getDate();
            LocalDate endDate = schedule.stream().max(Comparator.comparing(ScheduleCell::getDate)).orElseThrow(Exception::new).getDate();

            adminDao.editDoctorSchedule(startDate, endDate, doctorId, schedule);

            schedule.forEach(sc -> Assert.assertNotEquals(0, sc.getId()));
        } catch (Exception e) {
            Assert.fail();
        }

        return schedule;
    }


    protected static List<ScheduleCell> generateSchedule(List<Integer> durations, int daysCount, LocalDate startDate, Doctor doctor) {
        LocalTime startTime = LocalTime.now().withNano(0).withSecond(0);

        List<ScheduleCell> schedule = new ArrayList<>(daysCount);
        List<TimeCell> timeCells;
        LocalDate tempDate;
        LocalTime tempTime;

        for (int i = 0; i < daysCount; i++) {
            timeCells = new ArrayList<>(durations.size());
            tempDate = startDate.plusDays(i);

            for (int d : durations) {
                tempTime = startTime.plusMinutes(d);

                timeCells.add(new TimeCell(tempTime, null, d,
                        TicketFactory.buildTicketToDoctor(doctor.getId(), tempDate, tempTime)));
            }

            schedule.add(new ScheduleCell(0, doctor, tempDate, timeCells));
        }

        return schedule;
    }
}