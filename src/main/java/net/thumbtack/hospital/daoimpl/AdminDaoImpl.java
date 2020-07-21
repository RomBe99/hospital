package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.AdminDao;
import net.thumbtack.hospital.mapper.AdminMapper;
import net.thumbtack.hospital.mapper.UserTypes;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCodes;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component("AdminDaoImpl")
public class AdminDaoImpl extends UserDaoImpl implements AdminDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);
    private static final String CLASS_NAME = AdminDaoImpl.class.getSimpleName();

    private AdminMapper getAdminMapper(SqlSession session) {
        return session.getMapper(AdminMapper.class);
    }

    @Override
    public void insertAdministrator(Administrator administrator) {
        LOGGER.debug(CLASS_NAME + ": Insert administrator = {}", administrator);

        try (SqlSession session = getSession()) {
            try {
                int userTypeId = getCommonMapper(session).getUserTypeId(UserTypes.ADMINISTRATOR.getType());

                AdminMapper mapper = getAdminMapper(session);
                mapper.insertUser(administrator, userTypeId);
                mapper.insertAdministrator(administrator);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Administrator = {} successfully inserted", administrator);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't insert administrator = {}", administrator, ex);

                throw ex;
            }
        }
    }

    @Override
    public void updateAdministrator(Administrator administrator) {
        LOGGER.debug(CLASS_NAME + ": Update administrator = {}", administrator);

        try (SqlSession session = getSession()) {
            try {
                AdminMapper mapper = getAdminMapper(session);
                mapper.updateUser(administrator);
                mapper.updateAdministrator(administrator);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Administrator = {} successfully updated", administrator);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't update administrator = {}", administrator, ex);

                throw ex;
            }
        }
    }

    @Override
    public Administrator getAdministratorById(int id) {
        LOGGER.debug(CLASS_NAME + ": Get administrator with id = {}", id);

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.AdminMapper.getAdminById", id);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get administrator with id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public void removeAdministratorById(int id) {
        LOGGER.debug(CLASS_NAME + ": Remove administrator with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                getAdminMapper(session).removeAdministratorById(id);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Administrator with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't remove administrator with id = {}", id, ex);

                throw ex;
            }
        }
    }

    @Override
    public void editDoctorSchedule(LocalDate dateStart, LocalDate dateEnd, int doctorId, List<ScheduleCell> schedule) {
        LOGGER.debug(CLASS_NAME + ": Inserting schedule = {} for doctor with id = {} (date start = {} date end = {})",
                schedule, doctorId, dateStart, dateEnd);

        try (SqlSession session = getSession()) {
            try {
                AdminMapper mapper = getAdminMapper(session);

                for (ScheduleCell s : schedule) {
                    mapper.insertScheduleCell(doctorId, s);

                    for (TimeCell c : s.getCells()) {
                        mapper.insertTimeCell(s.getId(), c);
                    }
                }

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Schedule = {} for doctor with id = {} successfully inserted (date start = {} date end = {})",
                        schedule, doctorId, dateStart, dateEnd);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't insert schedule = {} for doctor with id = {} (date start = {} date end = {})",
                        schedule, doctorId, dateStart, dateEnd, ex);

                throw ex;
            }
        }
    }

    @Override
    public int hasPermissions(String sessionId) throws PermissionDeniedException {
        LOGGER.debug(CLASS_NAME + ": Checking administrator permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return getAdminMapper(session).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't check administrator permissions for session id = {}", sessionId, ex);

            throw new PermissionDeniedException(PermissionDeniedErrorCodes.PERMISSION_DENIED);
        }
    }
}