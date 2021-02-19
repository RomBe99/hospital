package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.mapper.UserMapper;
import net.thumbtack.hospital.model.user.Doctor;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Repository("UserDaoImpl")
public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public final int login(String sessionId, String login, String password) {
        LOGGER.debug("Login user with login = {} and password = {}, session id = {}", login, password, sessionId);

        try (SqlSession session = getSession()) {
            try {
                final UserMapper mapper = session.getMapper(UserMapper.class);
                final Integer userId = mapper.getUserIdByLoginAndPassword(login, password);
                mapper.loginUser(sessionId, userId);

                session.commit();
                LOGGER.debug("User with login = {} and password = {}, id = {}, session id = {} successfully logged in",
                        login, password, userId, session);

                return userId;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("Can't login user with login = {} and password = {}, session id = {}", login, password, sessionId);

                throw ex;
            }
        }
    }

    @Override
    public final void logout(String sessionId) {
        LOGGER.debug("Logout user with session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            try {
                session.getMapper(UserMapper.class).logoutUser(sessionId);

                session.commit();
                LOGGER.debug("User with session id = {} successfully logout", sessionId);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("User with session id = {} can't logout", sessionId, ex);

                throw ex;
            }
        }
    }

    @Override
    public Doctor getDoctorInformationWithoutSchedule(int doctorId) {
        LOGGER.debug("Get information by doctor id = {}", doctorId);

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.UserMapper.getDoctorsInformationWithoutScheduleById", doctorId);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get information by doctor id = {}", doctorId, ex);

            throw ex;
        }
    }

    @Override
    public List<Doctor> getDoctorsBySpecialityWithoutSchedule(String specialty) {
        LOGGER.debug("Get information about doctors who specialty is {}", specialty);

        try (SqlSession session = getSession()) {
            return session.selectList("net.thumbtack.hospital.mapper.UserMapper.getDoctorsInformationWithoutScheduleBySpecialty", specialty);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get information about doctors who specialty is {}", specialty, ex);

            throw ex;
        }
    }

    @Override
    public int hasPermissions(String sessionId) {
        LOGGER.debug("Checking user permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            final Integer userId = session.getMapper(UserMapper.class).hasPermissions(sessionId);

            return userId == null ? 0 : userId;
        } catch (RuntimeException ex) {
            LOGGER.error("Can't check user permissions for session id = {}", sessionId, ex);

            throw ex;
        }
    }

    @Override
    public Doctor getDoctorInformationWithSchedule(int patientId, int doctorId, LocalDate startDate, LocalDate endDate) {
        LOGGER.debug("Get information about doctor = {} for patient id = {} with schedule where start date = {} to end date = {}",
                doctorId, patientId, startDate, endDate);

        final Map<String, Object> params = new HashMap<>();
        params.put("patientId", patientId == 0 ? null : patientId);
        params.put("doctorId", doctorId == 0 ? null : doctorId);
        params.put("speciality", null);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.UserMapper.getDoctorsInformationWithSchedule", params);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get information about doctor = {} for patient id = {} with schedule where start date = {} to end date = {}",
                    doctorId, patientId, startDate, endDate);

            throw ex;
        }
    }

    @Override
    public List<Doctor> getDoctorsInformationWithSchedule(int patientId, String specialty, LocalDate startDate, LocalDate endDate) {
        LOGGER.debug("Get information about all doctors with speciality = {} for patient id = {} with schedule where start date = {} to end date = {}",
                specialty, patientId, startDate, endDate);

        final Map<String, Object> params = new HashMap<>();
        params.put("patientId", patientId == 0 ? null : patientId);
        params.put("doctorId", null);
        params.put("speciality", specialty);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        try (SqlSession session = getSession()) {
            return session.selectList("net.thumbtack.hospital.mapper.UserMapper.getDoctorsInformationWithSchedule", params);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get information about all doctors with speciality = {} for patient id = {} with schedule where start date = {} to end date = {}",
                    specialty, patientId, startDate, endDate);

            throw ex;
        }
    }
}