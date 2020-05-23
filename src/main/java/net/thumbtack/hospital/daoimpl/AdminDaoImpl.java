package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.AdminDao;
import net.thumbtack.hospital.mapper.AdminMapper;
import net.thumbtack.hospital.model.Administrator;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCodes;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("AdminDaoImpl")
public class AdminDaoImpl extends UserDaoImpl implements AdminDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);
    private static final String className = AdminDaoImpl.class.getSimpleName();

    private AdminMapper getAdminMapper(SqlSession session) {
        return session.getMapper(AdminMapper.class);
    }

    @Override
    public Administrator insertAdministrator(Administrator administrator) {
        LOGGER.debug(className + ": Insert administrator = {}", administrator);

        try (SqlSession session = getSession()) {
            try {
                AdminMapper mapper = getAdminMapper(session);
                mapper.insertUser(administrator);
                mapper.insertAdministrator(administrator);

                session.commit();
                LOGGER.debug(className + ": Administrator = {} successfully inserted", administrator);

                return administrator;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't insert administrator = {}", administrator, ex);

                throw ex;
            }
        }
    }

    @Override
    public void updateAdministrator(Administrator administrator) {
        LOGGER.debug(className + ": Update administrator = {}", administrator);

        try (SqlSession session = getSession()) {
            try {
                AdminMapper mapper = getAdminMapper(session);
                mapper.updateUser(administrator);
                mapper.updateAdministrator(administrator);

                session.commit();
                LOGGER.debug(className + ": Administrator = {} successfully updated", administrator);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't update administrator = {}", administrator, ex);

                throw ex;
            }
        }
    }

    @Override
    public Administrator getAdministratorById(int id) {
        LOGGER.debug(className + ": Get administrator with id = {}", id);

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.AdminMapper.getAdminById", id);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't get administrator with id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public void removeAdministratorById(int id) {
        LOGGER.debug(className + ": Remove administrator with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                getAdminMapper(session).removeAdministratorById(id);

                session.commit();
                LOGGER.debug(className + ": Administrator with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't remove administrator with id = {}", id, ex);

                throw ex;
            }
        }
    }

    @Override
    public int hasPermissions(String sessionId) throws PermissionDeniedException {
        LOGGER.debug(className + ": Checking administrator permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return getAdminMapper(session).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't check administrator permissions for session id = {}", sessionId, ex);

            throw new PermissionDeniedException(PermissionDeniedErrorCodes.PERMISSION_DENIED);
        }
    }
}