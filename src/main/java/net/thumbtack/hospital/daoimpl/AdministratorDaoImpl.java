package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.AdministratorDao;
import net.thumbtack.hospital.mapper.AdministratorMapper;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCode;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Component("AdministratorDaoImpl")
public class AdministratorDaoImpl implements AdministratorDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorDaoImpl.class);
    private static final String CLASS_NAME = AdministratorDaoImpl.class.getSimpleName();

    private final MapperFactory mapperFactory = new MapperFactory();

    @Override
    public void insertAdministrator(Administrator administrator) {
        LOGGER.debug(CLASS_NAME + ": Insert administrator = {}", administrator);

        try (SqlSession session = getSession()) {
            try {
                int userTypeId = mapperFactory.getMapper(session, CommonMapper.class).getUserTypeId(UserType.ADMINISTRATOR.getType());

                AdministratorMapper mapper = mapperFactory.getMapper(session, AdministratorMapper.class);
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
                AdministratorMapper mapper = mapperFactory.getMapper(session, AdministratorMapper.class);
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
            return session.selectOne("net.thumbtack.hospital.mapper.AdministratorMapper.getAdminById", id);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get administrator with id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public void removeAdministrator(int id) {
        LOGGER.debug(CLASS_NAME + ": Remove administrator with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                mapperFactory.getMapper(session, AdministratorMapper.class).removeAdministratorById(id);

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
    public int hasPermissions(String sessionId) throws PermissionDeniedException {
        LOGGER.debug(CLASS_NAME + ": Checking administrator permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return mapperFactory.getMapper(session, AdministratorMapper.class).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't check administrator permissions for session id = {}", sessionId, ex);

            throw new PermissionDeniedException(PermissionDeniedErrorCode.PERMISSION_DENIED);
        }
    }
}