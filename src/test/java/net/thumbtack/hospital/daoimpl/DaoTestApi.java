package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.*;
import net.thumbtack.hospital.debug.DebugDao;
import net.thumbtack.hospital.debug.DebugDaoImpl;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.ticket.Ticket;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.model.user.User;
import net.thumbtack.hospital.util.adapter.ScheduleTransformers;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import net.thumbtack.hospital.util.security.SecurityManagerImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class DaoTestApi {
    private final AdministratorDao administratorDao = new AdministratorDaoImpl();
    private final DoctorDao doctorDao = new DoctorDaoImpl();
    private final PatientDao patientDao = new PatientDaoImpl();
    private final UserDao userDao = new UserDaoImpl();
    private final MedicalCommissionDao medicalCommissionDao = new MedicalCommissionDaoImpl();
    private final ScheduleDao scheduleDao = new ScheduleDaoImpl();
    private final CommonDao commonDao = new CommonDaoImpl();
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

    public static String generateSessionId() {
        return new CookieFactory().produceCookie(CookieFactory.JAVA_SESSION_ID).getValue();
    }

    // Template methods

    public void insertUser(User user) {
        final Map<Class<? extends User>, Consumer<User>> userInsertOperations = new HashMap<>();
        userInsertOperations.put(Doctor.class, u -> doctorDao.insertDoctor((Doctor) u));
        userInsertOperations.put(Patient.class, u -> patientDao.insertPatient((Patient) u));
        userInsertOperations.put(Administrator.class, u -> administratorDao.insertAdministrator((Administrator) u));

        userInsertOperations.get(user.getClass()).accept(user);

        final User userAfterInsert = getUserById(user.getId(), user.getClass());
        Assert.assertEquals(user, userAfterInsert);
    }

    public void hasPermissions(String sessionId, int expectedUserId, UserType... userTypes) throws PermissionDeniedException {
        final int actualUserId = SecurityManagerImpl
                .getSecurityManager(userTypes)
                .hasPermission(sessionId);

        Assert.assertEquals(expectedUserId, actualUserId);
    }

    public User getUserById(int userId, Class<? extends User> userClass) {
        final Map<Class<? extends User>, Function<Integer, ? extends User>> userSuppliers = new HashMap<>();
        userSuppliers.put(Doctor.class, doctorDao::getDoctorById);
        userSuppliers.put(Patient.class, patientDao::getPatientById);
        userSuppliers.put(Administrator.class, administratorDao::getAdministratorById);

        return userSuppliers
                .get(userClass)
                .apply(userId);
    }

    public void updateUser(User user, String newPassword) {
        final Map<Class<? extends User>, Consumer<User>> userUpdaters = new HashMap<>();
        userUpdaters.put(Patient.class, u -> patientDao.updatePatient((Patient) u, newPassword));
        userUpdaters.put(Administrator.class, u -> administratorDao.updateAdministrator((Administrator) u, newPassword));

        userUpdaters.get(user.getClass()).accept(user);
        user.setPassword(newPassword);

        final User userAfterUpdate = getUserById(user.getId(), user.getClass());
        Assert.assertEquals(user, userAfterUpdate);
    }

    public void removeUserById(int userId, Class<? extends User> userType) {
        final Map<Class<? extends User>, Consumer<Integer>> userRemovers = new HashMap<>();
        userRemovers.put(Doctor.class, doctorDao::removeDoctor);
        userRemovers.put(Administrator.class, administratorDao::removeAdministrator);

        userRemovers.get(userType).accept(userId);

        final User userAfterDelete = getUserById(userId, userType);
        Assert.assertNull(userAfterDelete);
    }

    // User dao methods

    public String login(String login, String password) {
        final String sessionId = generateSessionId();
        final int actualUserId = userDao.login(sessionId, login, password);

        Assert.assertNotEquals(0, actualUserId);

        try {
            hasPermissions(sessionId, actualUserId);
        } catch (PermissionDeniedException ex) {
            Assert.fail();
        }

        return sessionId;
    }

    public String loginRootAdmin() {
        final String login = "admin";
        final String password = "admin";

        return login(login, password);
    }

    public void logout(String sessionId) {
        userDao.logout(sessionId);

        try {
            hasPermissions(sessionId, 0);
        } catch (PermissionDeniedException e) {
            Assert.fail();
        }
    }

    public Doctor getDoctorInformationWithSchedule(int patientId, int doctorId, LocalDate dateStart, LocalDate dateEnd) {
        return userDao.getDoctorInformationWithSchedule(patientId, doctorId, dateStart, dateEnd);
    }

    public List<Doctor> getDoctorsInformation(int patientId, String speciality, LocalDate dateStart, LocalDate dateEnd) {
        final List<Doctor> result = userDao.getDoctorsInformationWithSchedule(patientId, speciality, dateStart, dateEnd);
        result.sort(Comparator.comparingInt(User::getId));

        return result;
    }

    // Medical commission dao methods

    public void createMedicalCommission(TicketToMedicalCommission ticket) {
        ticket.getDoctorIds().sort(Integer::compareTo);
        medicalCommissionDao.createMedicalCommission(ticket);
        Assert.assertNotEquals(0, ticket.getId());

        final TicketToMedicalCommission insertedTicket = debugDao.getTicketToMedicalCommissionByTitle(ticket.getTitle());
        Assert.assertEquals(ticket, insertedTicket);
    }

    public void getTicketsToMedicalCommission(int patientId, List<TicketToMedicalCommission> expectedTickets) {
        final List<TicketToMedicalCommission> actualTickets = medicalCommissionDao.getTicketsToMedicalCommission(patientId);
        actualTickets.forEach(t -> t.getDoctorIds().sort(Integer::compareTo));

        expectedTickets.sort(Comparator.comparing(Ticket::getTime));
        expectedTickets.sort(Comparator.comparing(Ticket::getDate));

        Assert.assertEquals(expectedTickets, actualTickets);
    }

    public void denyMedicalCommission(String ticketTitle) {
        medicalCommissionDao.denyMedicalCommission(ticketTitle);

        TicketToMedicalCommission insertedTicket = debugDao.getTicketToMedicalCommissionByTitle(ticketTitle);
        Assert.assertNull(insertedTicket);
    }

    // Schedule dao methods

    public void scheduleInsertingChecker(int doctorId, LocalDate dateStart, LocalDate dateEnd,
                                         LocalTime timeStart, LocalTime timeEnd, List<ScheduleCell> expectedSchedule) {
        final List<ScheduleCell> actualSchedule = debugDao.getScheduleByDoctorId(doctorId, dateStart, dateEnd, timeStart, timeEnd);
        Assert.assertEquals(expectedSchedule.size(), actualSchedule.size());
        ScheduleTransformers.sortSchedule(actualSchedule);
        Assert.assertEquals(expectedSchedule, actualSchedule);
    }

    public void insertSchedule(int doctorId, List<ScheduleCell> schedule) {
        ScheduleTransformers.sortSchedule(schedule);
        scheduleDao.insertSchedule(doctorId, schedule);

        schedule.forEach(s -> Assert.assertNotEquals(0, s.getId()));

        final LocalDate dateStart = schedule.iterator().next().getDate();
        final LocalDate dateEnd = schedule.listIterator(schedule.size()).previous().getDate();
        scheduleInsertingChecker(doctorId, dateStart, dateEnd, null, null, schedule);
    }

    public void editSchedule(int doctorId, LocalDate dateStart, LocalDate dateEnd, List<ScheduleCell> newSchedule) {
        ScheduleTransformers.sortSchedule(newSchedule);
        scheduleDao.editSchedule(doctorId, dateStart, dateEnd, newSchedule);

        newSchedule.forEach(s -> Assert.assertNotEquals(0, s.getId()));

        scheduleInsertingChecker(doctorId, dateStart, dateEnd, null, null, newSchedule);
    }

    public void appointmentToDoctor(int patientId, String ticketTitle) {
        scheduleDao.appointmentToDoctor(patientId, ticketTitle);

        final boolean containsPatient = debugDao.containsPatientInTimeCell(patientId, ticketTitle);
        Assert.assertTrue(containsPatient);
    }

    public void denyTicket(int patientId, String ticketTitle) {
        scheduleDao.denyTicket(patientId, ticketTitle);

        final boolean containsPatient = debugDao.containsPatientInTimeCell(patientId, ticketTitle);
        Assert.assertFalse(containsPatient);
    }

    public List<TicketToDoctor> getTicketsToDoctor(int patientId) {
        final List<TicketToDoctor> result = scheduleDao.getTicketsToDoctor(patientId);
        result.sort(Comparator.comparing(Ticket::getTitle));

        return result;
    }

    // Common dao methods

    public void getUserTypeByUserId(int userId, String expectedUserType) {
        final String actualUserType = commonDao.getUserTypeByUserId(userId);

        Assert.assertEquals(expectedUserType, actualUserType);
    }
}