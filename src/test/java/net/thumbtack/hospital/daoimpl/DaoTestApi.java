package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.*;
import net.thumbtack.hospital.debug.DebugDao;
import net.thumbtack.hospital.debug.DebugDaoImpl;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.model.user.User;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import net.thumbtack.hospital.util.security.manager.SecurityManagerImpl;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class DaoTestApi {
    private final AdminDao adminDao = new AdminDaoImpl();
    private final DoctorDao doctorDao = new DoctorDaoImpl();
    private final PatientDao patientDao = new PatientDaoImpl();
    private final UserDao userDao = new UserDaoImpl();
    private final MedicalCommissionDao medicalCommissionDao = new MedicalCommissionDaoImpl();
    private final ScheduleDao scheduleDao = new ScheduleDaoImpl();
    private final DebugDao debugDao = new DebugDaoImpl();

    @BeforeClass
    public static void setUp() {
        MyBatisUtils.initConnection();
    }

    @Before
    public void clearDatabase() {
        debugDao.clearUsers();
        debugDao.clearLoggedInUsers();
        debugDao.clearAdministrators();
        debugDao.clearDoctors();
        debugDao.clearPatients();
        debugDao.clearMedicalCommissions();
        debugDao.clearCommissionDoctors();
        debugDao.clearScheduleCells();
        debugDao.clearTimeCells();
    }

    public static List<ScheduleCell> generateSchedule(int doctorId, int daysCount,
                                                      LocalTime startTime, LocalDate startDate, List<Integer> durations) {
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
                        TicketFactory.buildTicketToDoctor(doctorId, tempDate, tempTime)));
            }

            schedule.add(new ScheduleCell(0, null, tempDate, timeCells));
        }

        return schedule;
    }

    public static String generateSessionId() {
        return new CookieFactory().getCookieByCookieName(CookieFactory.JAVA_SESSION_ID).getValue();
    }

    // Template methods

    public void insertUser(User user) {
        Map<Class<? extends User>, Consumer<User>> userInsertOperations = new HashMap<>();
        userInsertOperations.put(Doctor.class, u -> doctorDao.insertDoctor((Doctor) u));
        userInsertOperations.put(Patient.class, u -> patientDao.insertPatient((Patient) u));
        userInsertOperations.put(Administrator.class, u -> adminDao.insertAdministrator((Administrator) u));

        userInsertOperations.get(user.getClass()).accept(user);

        User userAfterInsert = getUserById(user.getId(), user.getClass());
        Assert.assertEquals(user, userAfterInsert);
    }

    public void hasPermissions(String sessionId, int expectedUserId, UserType... userTypes) throws PermissionDeniedException {
        int actualUserId = SecurityManagerImpl
                .getSecurityManager(userTypes)
                .hasPermission(sessionId);

        Assert.assertEquals(expectedUserId, actualUserId);
    }

    public User getUserById(int userId, Class<? extends User> userClass) {
        Map<Class<? extends User>, Function<Integer, ? extends User>> userSuppliers = new HashMap<>();
        userSuppliers.put(Doctor.class, doctorDao::getDoctorById);
        userSuppliers.put(Patient.class, patientDao::getPatientById);
        userSuppliers.put(Administrator.class, adminDao::getAdministratorById);

        return userSuppliers
                .get(userClass)
                .apply(userId);
    }

    public void updateUser(User user) {
        Map<Class<? extends User>, Consumer<User>> userUpdaters = new HashMap<>();
        userUpdaters.put(Patient.class, u -> patientDao.updatePatient((Patient) u));
        userUpdaters.put(Administrator.class, u -> adminDao.updateAdministrator((Administrator) u));

        userUpdaters.get(user.getClass()).accept(user);

        User userAfterUpdate = getUserById(user.getId(), user.getClass());
        Assert.assertEquals(user, userAfterUpdate);
    }

    public void removeUserById(int userId, Class<? extends User> userType) {
        Map<Class<? extends User>, Consumer<Integer>> userRemovers = new HashMap<>();
        userRemovers.put(Doctor.class, doctorDao::removeDoctor);
        userRemovers.put(Administrator.class, adminDao::removeAdministrator);

        userRemovers.get(userType).accept(userId);

        User userAfterDelete = getUserById(userId, userType);
        Assert.assertNull(userAfterDelete);
    }

    // User dao methods

    public String login(String login, String password) {
        String sessionId = generateSessionId();
        int actualUserId = userDao.login(sessionId, login, password);

        Assert.assertNotEquals(0, actualUserId);

        try {
            hasPermissions(sessionId, actualUserId);
        } catch (PermissionDeniedException ex) {
            Assert.fail();
        }

        return sessionId;
    }

    public String loginRootAdmin() {
        String login = "admin";
        String password = "admin";

        return login(login, password);
    }

    public void logout(String sessionId) {
        userDao.logout(sessionId);

        try {
            hasPermissions(sessionId, 0);
            Assert.fail();
        } catch (PermissionDeniedException ignored) {
        }
    }

    public void getDoctorInformation(int patientId, int doctorId, LocalDate startDate, LocalDate endDate,
                                     Doctor expectedDoctor) {
        Doctor actualDoctor = userDao.getDoctorInformation(patientId, doctorId, startDate, endDate);

        Assert.assertEquals(expectedDoctor, actualDoctor);
    }

    public void getDoctorsInformation(int patientId, String speciality, LocalDate startDate, LocalDate endDate,
                                      List<Doctor> expectedDoctors) {
        List<Doctor> actualDoctors = userDao.getDoctorsInformation(patientId, speciality, startDate, endDate);

        actualDoctors.sort(Comparator.comparingInt(User::getId));
        expectedDoctors.sort(Comparator.comparingInt(User::getId));

        Assert.assertEquals(expectedDoctors, actualDoctors);
    }

    // Medical commission dao methods

    public void createMedicalCommission(TicketToMedicalCommission ticket) {
        medicalCommissionDao.createMedicalCommission(ticket);

        TicketToMedicalCommission insertedTicket = debugDao.getTicketToMedicalCommissionByTitle(ticket.getTitle());
        Assert.assertEquals(ticket, insertedTicket);
    }

    public List<TicketToMedicalCommission> getTicketsToMedicalCommission(int patientId) {
        return medicalCommissionDao.getTicketsToMedicalCommission(patientId);
    }

    public void denyMedicalCommission(String title) {
        medicalCommissionDao.denyMedicalCommission(title);

        TicketToMedicalCommission insertedTicket = debugDao.getTicketToMedicalCommissionByTitle(title);
        Assert.assertNull(insertedTicket);
    }

    // Schedule dao methods

    public void insertSchedule(int doctorId, List<ScheduleCell> schedule) {
        scheduleDao.insertSchedule(doctorId, schedule);

        schedule.forEach(s -> Assert.assertNotEquals(0, s.getId()));
    }

    public void editSchedule(int doctorId, LocalDate dateStart, LocalDate dateEnd, List<ScheduleCell> newSchedule) {
        scheduleDao.editSchedule(doctorId, dateStart, dateEnd, newSchedule);

        newSchedule.forEach(s -> Assert.assertNotEquals(0, s.getId()));
    }

    public void appointmentToDoctor(int patientId, String ticketTitle) {
        scheduleDao.appointmentToDoctor(patientId, ticketTitle);

        boolean containsPatient = debugDao.containsPatientInTimeCell(patientId, ticketTitle);
        Assert.assertTrue(containsPatient);
    }

    public void denyTicket(int patientId, String ticketTitle) {
        scheduleDao.denyTicket(patientId, ticketTitle);

        boolean containsPatient = debugDao.containsPatientInTimeCell(patientId, ticketTitle);
        Assert.assertFalse(containsPatient);
    }

    public List<TicketToDoctor> getTicketsToDoctor(int patientId) {
        return scheduleDao.getTicketsToDoctor(patientId);
    }
}