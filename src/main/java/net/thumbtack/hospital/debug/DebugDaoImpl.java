package net.thumbtack.hospital.debug;

import net.thumbtack.hospital.daoimpl.BaseDaoImpl;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("DebugDaoImpl")
public class DebugDaoImpl extends BaseDaoImpl implements DebugDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugDaoImpl.class);
    private static final String className = DebugDaoImpl.class.getSimpleName();

    private DebugMapper getDebugMapper(SqlSession session) {
        return session.getMapper(DebugMapper.class);
    }

    @Override
    public void clearUsers() {
        try (SqlSession session = getSession()) {
            LOGGER.debug(className + ": Clear all users from database");

            try {
                getDebugMapper(session).clearUsers();
                session.commit();
                LOGGER.debug(className + ": All users is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error(className + ": Can't clear users from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearAdministrators() {
        try (SqlSession session = getSession()) {
            LOGGER.debug(className + ": Clear all administrators from database");

            try {
                getDebugMapper(session).clearAdministrators();
                session.commit();
                LOGGER.debug(className + ": All administrators is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error(className + ": Can't clear user data from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearDoctors() {
        try (SqlSession session = getSession()) {
            LOGGER.debug(className + ": Clear all doctors from database");

            try {
                getDebugMapper(session).clearDoctors();
                session.commit();
                LOGGER.debug(className + ": All doctors is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error(className + ": Can't clear doctors from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearPatients() {
        try (SqlSession session = getSession()) {
            LOGGER.debug(className + ": Clear all patients from database");

            try {
                getDebugMapper(session).clearPatients();
                session.commit();
                LOGGER.debug(className + ": All patients is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error(className + ": Can't clear patients from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearLoggedInUsers() {
        try (SqlSession session = getSession()) {
            LOGGER.debug(className + ": Clear all logged in users from database");

            try {
                getDebugMapper(session).clearLoggedInUsers();
                session.commit();
                LOGGER.debug(className + ": All logged in users is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error(className + ": Can't clear logged in users from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearScheduleCells() {
        try (SqlSession session = getSession()) {
            LOGGER.debug(className + ": Clear all schedule cells from database");

            try {
                getDebugMapper(session).clearScheduleCells();
                session.commit();
                LOGGER.debug(className + ": All schedule cells is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error(className + ": Can't clear schedule cells from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearTimeCells() {
        try (SqlSession session = getSession()) {
            LOGGER.debug(className + ": Clear all time cells from database");

            try {
                getDebugMapper(session).clearTimeCells();
                session.commit();
                LOGGER.debug(className + ": All time cells is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error(className + ": Can't clear time cells from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearMedicalCommissions() {
        try (SqlSession session = getSession()) {
            LOGGER.debug(className + ": Clear all medical commissions from database");

            try {
                getDebugMapper(session).clearMedicalCommissions();
                session.commit();
                LOGGER.debug(className + ": All medical commissions is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error(className + ": Can't clear medical commissions from database", ex);

                throw ex;
            }
        }
    }

    @Override
    public void clearCommissionDoctors() {
        try (SqlSession session = getSession()) {
            LOGGER.debug(className + ": Clear all commissions doctors from database");

            try {
                getDebugMapper(session).clearCommissionDoctors();
                session.commit();
                LOGGER.debug(className + ": All commissions doctors is cleared");
            } catch (RuntimeException ex) {
                LOGGER.error(className + ": Can't clear commissions doctors from database", ex);

                throw ex;
            }
        }
    }
}