package net.thumbtack.hospital.debug;

import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Component("DebugDaoImpl")
public class DebugDaoImpl implements DebugDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugDaoImpl.class);

    @Override
    public void clearUsers() {
        try (SqlSession session = getSession()) {
            LOGGER.debug("Clear all users from database");

            try {
                session.getMapper(DebugMapper.class).clearUsers();

                session.commit();
                LOGGER.debug("All users is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error("Can't clear users from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearAdministrators() {
        try (SqlSession session = getSession()) {
            LOGGER.debug("Clear all administrators from database");

            try {
                session.getMapper(DebugMapper.class).clearAdministrators();

                session.commit();
                LOGGER.debug("All administrators is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error("Can't clear user data from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearDoctors() {
        try (SqlSession session = getSession()) {
            LOGGER.debug("Clear all doctors from database");

            try {
                session.getMapper(DebugMapper.class).clearDoctors();

                session.commit();
                LOGGER.debug("All doctors is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error("Can't clear doctors from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearPatients() {
        try (SqlSession session = getSession()) {
            LOGGER.debug("Clear all patients from database");

            try {
                session.getMapper(DebugMapper.class).clearPatients();

                session.commit();
                LOGGER.debug("All patients is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error("Can't clear patients from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearLoggedInUsers() {
        try (SqlSession session = getSession()) {
            LOGGER.debug("Clear all logged in users from database");

            try {
                session.getMapper(DebugMapper.class).clearLoggedInUsers();

                session.commit();
                LOGGER.debug("All logged in users is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error("Can't clear logged in users from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearScheduleCells() {
        try (SqlSession session = getSession()) {
            LOGGER.debug("Clear all schedule cells from database");

            try {
                session.getMapper(DebugMapper.class).clearScheduleCells();

                session.commit();
                LOGGER.debug("All schedule cells is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error("Can't clear schedule cells from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearTimeCells() {
        try (SqlSession session = getSession()) {
            LOGGER.debug("Clear all time cells from database");

            try {
                session.getMapper(DebugMapper.class).clearTimeCells();

                session.commit();
                LOGGER.debug("All time cells is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error("Can't clear time cells from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearMedicalCommissions() {
        try (SqlSession session = getSession()) {
            LOGGER.debug("Clear all medical commissions from database");

            try {
                session.getMapper(DebugMapper.class).clearMedicalCommissions();

                session.commit();
                LOGGER.debug("All medical commissions is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error("Can't clear medical commissions from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearCommissionDoctors() {
        try (SqlSession session = getSession()) {
            LOGGER.debug("Clear all commissions doctors from database");

            try {
                session.getMapper(DebugMapper.class).clearCommissionDoctors();

                session.commit();
                LOGGER.debug("All commissions doctors is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error("Can't clear commissions doctors from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public boolean containsPatientInTimeCell(int patientId, String ticketTitle) {
        LOGGER.debug("Checking time cell with ticket title = {} for patient = {} record", ticketTitle, patientId);

        try (SqlSession session = getSession()) {
            return session.getMapper(DebugMapper.class).containsPatientInTimeCell(patientId, ticketTitle);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't check time cell with ticket title = {} for patient = {} record", ticketTitle, patientId, ex);

            throw ex;
        }
    }

    @Override
    public List<ScheduleCell> getScheduleByDoctorId(int doctorId, LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd) {
        LOGGER.debug("Try get schedule for doctor with id = {} " +
                        "where start date = {} and end date = {} with start time = {} and end time = {}",
                doctorId, dateStart, dateEnd, timeStart, timeEnd);

        Map<String, Object> params = new HashMap<>();
        params.put("doctorId", doctorId);
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        params.put("timeStart", timeStart);
        params.put("timeEnd", timeEnd);

        try (SqlSession session = getSession()) {
            return session.selectList("net.thumbtack.hospital.debug.DebugMapper.getScheduleByDoctorId", params);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get schedule for doctor with id = {} " +
                            "where start date = {} and end date = {} with start time = {} and end time = {}",
                    doctorId, dateStart, dateEnd, timeStart, timeEnd, ex);

            throw ex;
        }
    }

    @Override
    public TicketToMedicalCommission getTicketToMedicalCommissionByTitle(String title) {
        LOGGER.debug("Get ticket to medical commission with title = {}", title);

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.debug.DebugMapper.getMedicalCommissionTicketByTitle", title);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get ticket to medical commission with title = {}", title, ex);

            throw ex;
        }
    }
}