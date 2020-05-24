package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.mapper.UserMapper;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCodes;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("UserDaoImpl")
public class UserDaoImpl extends BaseDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);
    private static final String className = UserDaoImpl.class.getSimpleName();

    private UserMapper getUserMapper(SqlSession session) {
        return session.getMapper(UserMapper.class);
    }

    @Override
    public final int login(String sessionId, String login, String password) {
        LOGGER.debug(className + ": Login user with login = {} and password = {}, session id = {}", login, password, sessionId);

        try (SqlSession session = getSession()) {
            try {
                UserMapper mapper = getUserMapper(session);
                int userId = mapper.getUserIdByLoginAndPassword(login, password);
                mapper.loginUser(sessionId, userId);

                session.commit();
                LOGGER.debug(className + ": User with login = {} and password = {}, id = {}, session id = {} successfully logged in",
                        login, password, userId, session);

                return userId;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't login user with login = {} and password = {}, session id = {}", login, password, sessionId);

                throw ex;
            }
        }
    }

    @Override
    public final void logout(String sessionId) {
        LOGGER.debug(className + ": Logout user with session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            getUserMapper(session).logoutUser(sessionId);

            LOGGER.debug(className + ": User with session id = {} successfully logout", sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": User with session id = {} can't logout", sessionId, ex);

            throw ex;
        }
    }

    @Override
    public int hasPermissions(String sessionId) throws PermissionDeniedException {
        LOGGER.debug(className + ": Checking user permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return getUserMapper(session).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't check user permissions for session id = {}", sessionId, ex);

            throw new PermissionDeniedException(PermissionDeniedErrorCodes.PERMISSION_DENIED);
        }
    }
}