package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.AdminDao;
import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.mapper.AdminMapper;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCode;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Component("AdminDaoImpl")
public class AdminDaoImpl implements AdminDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);
    private static final String CLASS_NAME = AdminDaoImpl.class.getSimpleName();

    private final MapperFactory mapperFactory = new MapperFactory();
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void insertAdministrator(Administrator administrator) {
        LOGGER.debug(CLASS_NAME + ": Insert administrator = {}", administrator);

        try (SqlSession session = getSession()) {
            try {
                int userTypeId = mapperFactory.getMapper(session, CommonMapper.class).getUserTypeId(UserType.ADMINISTRATOR.getType());

                AdminMapper mapper = mapperFactory.getMapper(session, AdminMapper.class);
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
                AdminMapper mapper = mapperFactory.getMapper(session, AdminMapper.class);
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
                mapperFactory.getMapper(session, AdminMapper.class).removeAdministratorById(id);

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
    public int login(String sessionId, String login, String password) {
        return userDao.login(sessionId, login, password);
    }

    @Override
    public void logout(String sessionId) {
        userDao.logout(sessionId);
    }

    @Override
    public int hasPermissions(String sessionId) throws PermissionDeniedException {
        LOGGER.debug(CLASS_NAME + ": Checking administrator permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return mapperFactory.getMapper(session, AdminMapper.class).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't check administrator permissions for session id = {}", sessionId, ex);

            throw new PermissionDeniedException(PermissionDeniedErrorCode.PERMISSION_DENIED);
        }
    }

    @Override
    public Doctor getDoctorInformation(int patientId, int doctorId, LocalDate startDate, LocalDate endDate) {
        return userDao.getDoctorInformation(patientId, doctorId, startDate, endDate);
    }

    @Override
    public List<Doctor> getDoctorsInformation(int patientId, String speciality, LocalDate startDate, LocalDate endDate) {
        return userDao.getDoctorsInformation(patientId, speciality, startDate, endDate);
    }
}