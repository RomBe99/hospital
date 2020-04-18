package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.model.Doctor;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoImpl extends BaseDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    private static final String className = UserDaoImpl.class.getSimpleName();

    @Override
    public List<Doctor> getAllDoctors() {
        LOGGER.debug(className + ": Get all doctors");

        try (SqlSession session = getSession()) {
            return getUserMapper(session).getAllDoctors();
        } catch (RuntimeException ex) {
            LOGGER.debug(className + ": Can't get all doctors", ex);

            throw ex;
        }
    }

    @Override
    public Doctor getDoctorById(int id) {
        LOGGER.debug(className + ": Get doctor by id");

        try (SqlSession session = getSession()) {
            return getUserMapper(session).getDoctorById(id);
        } catch (RuntimeException ex) {
            LOGGER.debug(className + ": Can't get doctor by id", ex);

            throw ex;
        }
    }
}