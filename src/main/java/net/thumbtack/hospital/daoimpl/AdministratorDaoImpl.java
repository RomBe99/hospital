package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.AdministratorDao;
import net.thumbtack.hospital.mapper.AdministratorMapper;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Administrator;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Repository("AdministratorDaoImpl")
public class AdministratorDaoImpl implements AdministratorDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorDaoImpl.class);

    @Override
    public void insertAdministrator(Administrator administrator) {
        LOGGER.debug("Insert administrator = {}", administrator);

        try (SqlSession session = getSession()) {
            try {
                final Integer userTypeId = session.getMapper(CommonMapper.class).getUserTypeId(UserType.ADMINISTRATOR.getType());

                final AdministratorMapper mapper = session.getMapper(AdministratorMapper.class);
                mapper.insertUser(administrator, userTypeId);
                mapper.insertAdministrator(administrator);

                session.commit();
                LOGGER.debug("Administrator = {} successfully inserted", administrator);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("Can't insert administrator = {}", administrator, ex);

                throw ex;
            }
        }
    }

    @Override
    public void updateAdministrator(Administrator administrator, String newPassword) {
        LOGGER.debug("Update administrator = {}", administrator);

        try (SqlSession session = getSession()) {
            try {
                final AdministratorMapper mapper = session.getMapper(AdministratorMapper.class);
                mapper.updateUser(administrator, newPassword);
                mapper.updateAdministrator(administrator);

                session.commit();
                LOGGER.debug("Administrator = {} successfully updated", administrator);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("Can't update administrator = {}", administrator, ex);

                throw ex;
            }
        }
    }

    @Override
    public Administrator getAdministratorById(int id) {
        LOGGER.debug("Get administrator with id = {}", id);

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.AdministratorMapper.getAdminById", id);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get administrator with id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public void removeAdministrator(int id) {
        LOGGER.debug("Remove administrator with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                session.getMapper(AdministratorMapper.class).removeAdministratorById(id);

                session.commit();
                LOGGER.debug("Administrator with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("Can't remove administrator with id = {}", id, ex);

                throw ex;
            }
        }
    }

    @Override
    public int hasPermissions(String sessionId) {
        LOGGER.debug("Checking administrator permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            final Integer userId = session.getMapper(AdministratorMapper.class).hasPermissions(sessionId);

            return userId == null ? 0 : userId;
        } catch (RuntimeException ex) {
            LOGGER.error("Can't check administrator permissions for session id = {}", sessionId, ex);

            throw ex;
        }
    }
}