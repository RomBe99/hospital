package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.AdminDao;
import net.thumbtack.hospital.model.Administrator;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdminDaoImpl extends BaseDaoImpl implements AdminDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);
    private static final String className = AdminDaoImpl.class.getSimpleName();

    @Override
    public Administrator insertAdministrator(Administrator administrator) {
        LOGGER.debug(className + ": Insert administrator = {}", administrator);

        try (SqlSession session = super.getSession()) {
            try {
                super.getUserMapper(session).insertUser(administrator);
                super.getAdminMapper(session).insertAdministrator(administrator);

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

        try (SqlSession session = super.getSession()) {
            try {
                super.getUserMapper(session).updateUser(administrator);
                super.getAdminMapper(session).updateAdministrator(administrator);

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

        try (SqlSession session = super.getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.AdminMapper.getAdminById", id);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't get administrator with id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public void removeAdministratorById(int id) {
        LOGGER.debug(className + ": Remove administrator with id = {}", id);

        try (SqlSession session = super.getSession()) {
            try {
                super.getAdminMapper(session).removeAdministratorById(id);

                session.commit();
                LOGGER.debug(className + ": Administrator with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't remove administrator with id = {}", id, ex);

                throw ex;
            }
        }
    }
}