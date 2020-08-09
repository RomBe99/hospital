package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.DoctorMapper;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.UserType;
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

@Component("DoctorDaoImpl")
public class DoctorDaoImpl implements DoctorDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);
    private static final String CLASS_NAME = DoctorDaoImpl.class.getSimpleName();

    private final MapperFactory mapperFactory = new MapperFactory();
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public Doctor insertDoctor(Doctor doctor) {
        LOGGER.debug(CLASS_NAME + ": Insert doctor = {}", doctor);

        try (SqlSession session = getSession()) {
            try {
                CommonMapper commonMapper = mapperFactory.getMapper(session, CommonMapper.class);
                int userTypeId = commonMapper.getUserTypeId(UserType.DOCTOR.getType());
                int cabinetId = commonMapper.getCabinetIdByName(doctor.getCabinet());
                int specialityId = commonMapper.getDoctorSpecialityIdByName(doctor.getSpecialty());

                DoctorMapper doctorMapper = mapperFactory.getMapper(session, DoctorMapper.class);
                doctorMapper.insertUser(doctor, userTypeId);
                doctorMapper.insertDoctor(doctor.getId(), specialityId, cabinetId);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Doctor = {} successfully inserted", doctor);

                return doctor;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't insert doctor = {}", doctor, ex);

                throw ex;
            }
        }
    }

    @Override
    public void removeDoctor(int id) {
        LOGGER.debug(CLASS_NAME + ": Delete doctor with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                mapperFactory.getMapper(session, DoctorMapper.class).removeDoctor(id);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Doctor with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't remove doctor with id = {}", id, ex);

                throw ex;
            }
        }
    }

    @Override
    public List<Doctor> getAllDoctors() {
        LOGGER.debug(CLASS_NAME + ": Get all doctors");

        try (SqlSession session = getSession()) {
            return session.selectList("net.thumbtack.hospital.mapper.DoctorMapper.getAllDoctors");
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get all doctors", ex);

            throw ex;
        }
    }

    @Override
    public Doctor getDoctorById(int id) {
        LOGGER.debug(CLASS_NAME + ": Get doctor by id");

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.DoctorMapper.getDoctorById", id);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get doctor by id", ex);

            throw ex;
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
        LOGGER.debug(CLASS_NAME + ": Checking doctor permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return mapperFactory.getMapper(session, DoctorMapper.class).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't check doctor permissions for session id = {}", sessionId, ex);

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