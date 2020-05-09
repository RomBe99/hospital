package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.UserDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl extends BaseDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);
    private static final String className = UserDaoImpl.class.getSimpleName();

    @Override
    public int login(String login, String password) {
        LOGGER.debug(className + ": Try login user with login = {} and password = {}", login, password);

        try (SqlSession session = super.getSession()) {
            int id = super.getUserMapper(session).tryLoginUser(login, password);

            LOGGER.debug(className + ": User with login = {} and password = {}, id = {}", login, password, id);

            return id;
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't login user with login = {} and password = {}", login, password);

            throw ex;
        }
    }
}