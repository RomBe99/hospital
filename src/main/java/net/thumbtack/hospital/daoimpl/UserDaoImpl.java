package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.UserMapper;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCode;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Component("UserDaoImpl")
public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    private static final String CLASS_NAME = UserDaoImpl.class.getSimpleName();

    private final MapperFactory mapperFactory = new MapperFactory();

    @Override
    public final int login(String sessionId, String login, String password) {
        LOGGER.debug(CLASS_NAME + ": Login user with login = {} and password = {}, session id = {}", login, password, sessionId);

        try (SqlSession session = getSession()) {
            try {
                UserMapper mapper = mapperFactory.getMapper(session, UserMapper.class);
                int userId = mapper.getUserIdByLoginAndPassword(login, password);
                mapper.loginUser(sessionId, userId);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": User with login = {} and password = {}, id = {}, session id = {} successfully logged in",
                        login, password, userId, session);

                return userId;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't login user with login = {} and password = {}, session id = {}", login, password, sessionId);

                throw ex;
            }
        }
    }

    @Override
    public final void logout(String sessionId) {
        LOGGER.debug(CLASS_NAME + ": Logout user with session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            try {
                mapperFactory.getMapper(session, UserMapper.class).logoutUser(sessionId);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": User with session id = {} successfully logout", sessionId);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": User with session id = {} can't logout", sessionId, ex);

                throw ex;
            }
        }
    }

    @Override
    public int hasPermissions(String sessionId) throws PermissionDeniedException {
        LOGGER.debug(CLASS_NAME + ": Checking user permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return mapperFactory.getMapper(session, UserMapper.class).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't check user permissions for session id = {}", sessionId, ex);

            throw new PermissionDeniedException(PermissionDeniedErrorCode.PERMISSION_DENIED);
        }
    }

    @Override
    public Doctor getDoctorInformation(int patientId, int doctorId, LocalDate startDate, LocalDate endDate) {
        LOGGER.debug(CLASS_NAME + ": Get information about doctor = {} for patient id = {} with schedule where start date = {} to end date = {}",
                doctorId, patientId, startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("patientId", patientId);
        params.put("doctorId", doctorId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.UserMapper.getDoctorInformation", params);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get information about doctor = {} for patient id = {} with schedule where start date = {} to end date = {}",
                    doctorId, patientId, startDate, endDate);

            throw ex;
        }
    }

    @Override
    public List<Doctor> getDoctorsInformation(int patientId, String speciality, LocalDate startDate, LocalDate endDate) {
        LOGGER.debug(CLASS_NAME + ": Get information about all doctors with speciality = {} for patient id = {} with schedule where start date = {} to end date = {}",
                speciality, patientId, startDate, endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("patientId", patientId);
        params.put("speciality", speciality);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        try (SqlSession session = getSession()) {
            return session.selectList("net.thumbtack.hospital.mapper.UserMapper.getDoctorsInformation", params);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get information about all doctors with speciality = {} for patient id = {} with schedule where start date = {} to end date = {}",
                    speciality, patientId, startDate, endDate);

            throw ex;
        }
    }
}