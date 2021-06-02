package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.*;
import net.thumbtack.hospital.debug.DebugDao;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.ticket.Ticket;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.model.user.User;
import net.thumbtack.hospital.server.HospitalApplication;
import net.thumbtack.hospital.util.ScheduleGenerator;
import net.thumbtack.hospital.util.adapter.ScheduleTransformer;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import net.thumbtack.hospital.util.security.SecurityManagerImpl;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootTest(classes = HospitalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class DaoTestApi {
    @Autowired
    private AdministratorDao administratorDao;
    @Autowired
    private DoctorDao doctorDao;
    @Autowired
    private PatientDao patientDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MedicalCommissionDao medicalCommissionDao;
    @Autowired
    private ScheduleDao scheduleDao;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private DebugDao debugDao;
    @Autowired
    private CookieFactory cookieFactory;
    @Autowired
    private TicketFactory ticketFactory;
    @Autowired
    private ScheduleTransformer scheduleTransformer;
    @Autowired
    private ScheduleGenerator scheduleGenerator;

    @BeforeAll
    public static void setUp() {
        MyBatisUtils.initConnection();
    }

    @BeforeEach
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

    protected String generateSessionId() {
        return cookieFactory.produceCookie(CookieFactory.JAVA_SESSION_ID).getValue();
    }

    public TicketFactory getTicketFactory() {
        return ticketFactory;
    }

    public ScheduleGenerator getScheduleGenerator() {
        return scheduleGenerator;
    }

    // Template methods

    public void insertUser(User user) {
        final var userInsertOperations = new HashMap<Class<? extends User>, Consumer<User>>();
        userInsertOperations.put(Doctor.class, u -> doctorDao.insertDoctor((Doctor) u));
        userInsertOperations.put(Patient.class, u -> patientDao.insertPatient((Patient) u));
        userInsertOperations.put(Administrator.class, u -> administratorDao.insertAdministrator((Administrator) u));

        userInsertOperations.get(user.getClass()).accept(user);

        final var userAfterInsert = getUserById(user.getId(), user.getClass());
        Assertions.assertEquals(user, userAfterInsert);
    }

    public void hasPermissions(String sessionId, int expectedUserId, UserType... userTypes) throws PermissionDeniedException {
        final var actualUserId = SecurityManagerImpl
                .getSecurityManager(userTypes)
                .hasPermission(sessionId);

        Assertions.assertEquals(expectedUserId, actualUserId);
    }

    public User getUserById(int userId, Class<? extends User> userClass) {
        final var userSuppliers = new HashMap<Class<? extends User>, Function<Integer, ? extends User>>();
        userSuppliers.put(Doctor.class, doctorDao::getDoctorById);
        userSuppliers.put(Patient.class, patientDao::getPatientById);
        userSuppliers.put(Administrator.class, administratorDao::getAdministratorById);

        return userSuppliers
                .get(userClass)
                .apply(userId);
    }

    public void updateUser(User user, String newPassword) {
        final var userUpdaters = new HashMap<Class<? extends User>, Consumer<User>>();
        userUpdaters.put(Patient.class, u -> patientDao.updatePatient((Patient) u, newPassword));
        userUpdaters.put(Administrator.class, u -> administratorDao.updateAdministrator((Administrator) u, newPassword));

        userUpdaters.get(user.getClass()).accept(user);
        user.setPassword(newPassword);

        final var userAfterUpdate = getUserById(user.getId(), user.getClass());
        Assertions.assertEquals(user, userAfterUpdate);
    }

    public void removeUserById(int userId, Class<? extends User> userType) {
        final var userRemovers = new HashMap<Class<? extends User>, Consumer<Integer>>();
        userRemovers.put(Doctor.class, doctorDao::removeDoctor);
        userRemovers.put(Administrator.class, administratorDao::removeAdministrator);

        userRemovers.get(userType).accept(userId);

        final var userAfterDelete = getUserById(userId, userType);
        Assertions.assertNull(userAfterDelete);
    }

    // User dao methods

    public String login(String login, String password) {
        final var sessionId = generateSessionId();
        final var actualUserId = userDao.login(sessionId, login, password);

        Assertions.assertNotEquals(0, actualUserId);

        try {
            hasPermissions(sessionId, actualUserId);
        } catch (PermissionDeniedException ex) {
            Assertions.fail();
        }

        return sessionId;
    }

    public String loginRootAdmin() {
        final var login = "admin";
        final var password = "admin";

        return login(login, password);
    }

    public void logout(String sessionId) {
        userDao.logout(sessionId);

        try {
            hasPermissions(sessionId, 0);
        } catch (PermissionDeniedException e) {
            Assertions.fail();
        }
    }

    public Doctor getDoctorInformationWithSchedule(int patientId, int doctorId, LocalDate dateStart, LocalDate dateEnd) {
        return userDao.getDoctorInformationWithSchedule(patientId, doctorId, dateStart, dateEnd);
    }

    public List<Doctor> getDoctorsInformation(int patientId, String speciality, LocalDate dateStart, LocalDate dateEnd) {
        final var result = userDao.getDoctorsInformationWithSchedule(patientId, speciality, dateStart, dateEnd);
        result.sort(Comparator.comparingInt(User::getId));

        return result;
    }

    // Medical commission dao methods

    public void createMedicalCommission(TicketToMedicalCommission ticket) {
        ticket.getDoctorIds().sort(Integer::compareTo);
        medicalCommissionDao.createMedicalCommission(ticket);
        Assertions.assertNotEquals(0, ticket.getId());

        final var insertedTicket = debugDao.getTicketToMedicalCommissionByTitle(ticket.getTitle());
        Assertions.assertEquals(ticket, insertedTicket);
    }

    public void getTicketsToMedicalCommission(int patientId, List<TicketToMedicalCommission> expectedTickets) {
        final var actualTickets = medicalCommissionDao.getTicketsToMedicalCommission(patientId);
        actualTickets.forEach(t -> t.getDoctorIds().sort(Integer::compareTo));

        expectedTickets.sort(Comparator.comparing(Ticket::getTime));
        expectedTickets.sort(Comparator.comparing(Ticket::getDate));

        Assertions.assertEquals(expectedTickets, actualTickets);
    }

    public void denyMedicalCommission(String ticketTitle) {
        medicalCommissionDao.denyMedicalCommission(ticketTitle);

        TicketToMedicalCommission insertedTicket = debugDao.getTicketToMedicalCommissionByTitle(ticketTitle);
        Assertions.assertNull(insertedTicket);
    }

    // Schedule dao methods

    public void scheduleInsertingChecker(int doctorId, LocalDate dateStart, LocalDate dateEnd,
                                         LocalTime timeStart, LocalTime timeEnd, List<ScheduleCell> expectedSchedule) {
        final var actualSchedule = debugDao.getScheduleByDoctorId(doctorId, dateStart, dateEnd, timeStart, timeEnd);
        Assertions.assertEquals(expectedSchedule.size(), actualSchedule.size());
        scheduleTransformer.sortSchedule(actualSchedule);
        Assertions.assertEquals(expectedSchedule, actualSchedule);
    }

    public void insertSchedule(int doctorId, List<ScheduleCell> schedule) {
        scheduleTransformer.sortSchedule(schedule);
        scheduleDao.insertSchedule(doctorId, schedule);

        schedule.forEach(s -> Assertions.assertNotEquals(0, s.getId()));

        final var dateStart = schedule.iterator().next().getDate();
        final var dateEnd = schedule.listIterator(schedule.size()).previous().getDate();
        scheduleInsertingChecker(doctorId, dateStart, dateEnd, null, null, schedule);
    }

    public void editSchedule(int doctorId, LocalDate dateStart, LocalDate dateEnd, List<ScheduleCell> newSchedule) {
        scheduleTransformer.sortSchedule(newSchedule);
        scheduleDao.editSchedule(doctorId, dateStart, dateEnd, newSchedule);

        newSchedule.forEach(s -> Assertions.assertNotEquals(0, s.getId()));

        scheduleInsertingChecker(doctorId, dateStart, dateEnd, null, null, newSchedule);
    }

    public void appointmentToDoctor(int patientId, String ticketTitle) {
        scheduleDao.appointmentToDoctor(patientId, ticketTitle);

        final var containsPatient = debugDao.containsPatientInTimeCell(patientId, ticketTitle);
        Assertions.assertTrue(containsPatient);
    }

    public void denyTicket(int patientId, String ticketTitle) {
        scheduleDao.denyTicket(patientId, ticketTitle);

        final var containsPatient = debugDao.containsPatientInTimeCell(patientId, ticketTitle);
        Assertions.assertFalse(containsPatient);
    }

    public List<TicketToDoctor> getTicketsToDoctor(int patientId) {
        final var result = scheduleDao.getTicketsToDoctor(patientId);
        result.sort(Comparator.comparing(Ticket::getTitle));

        return result;
    }

    // Common dao methods

    public UserType getUserTypeByUserId(int userId) {
        final var actualUserType = commonDao.getUserTypeByUserId(userId);

        return UserType.of(actualUserType);
    }
}