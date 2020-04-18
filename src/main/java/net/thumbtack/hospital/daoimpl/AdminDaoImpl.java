package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.AdminDao;
import net.thumbtack.hospital.model.Administrator;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.ScheduleCell;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AdminDaoImpl extends BaseDaoImpl implements AdminDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);
    private static final String className = AdminDaoImpl.class.getSimpleName();

    @Override
    public Administrator insertAdministrator(Administrator administrator) {
        LOGGER.debug(className + ": Insert administrator = {}", administrator);

        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).insertUser(administrator);
                getAdminMapper(session).insertAdministrator(administrator);

                session.commit();
                LOGGER.debug(className + ": Administrator = {} successfully inserted", administrator);

                return administrator;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.debug(className + ": Can't insert administrator = {}", administrator, ex);

                throw ex;
            }
        }
    }

    @Override
    public Doctor insertDoctor(Doctor doctor) {
        LOGGER.debug(className + ": Insert doctor = {}", doctor);

        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).insertUser(doctor);
                getAdminMapper(session).insertDoctor(doctor);

                session.commit();
                LOGGER.debug(className + ": Doctor = {} successfully inserted", doctor);

                return doctor;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.debug(className + ": Can't insert doctor = {}", doctor, ex);

                throw ex;
            }
        }
    }

    @Override
    public void updateAdministrator(Administrator administrator) {
        LOGGER.debug(className + ": Update administrator = {}", administrator);

        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).updateUser(administrator);
                getAdminMapper(session).updateAdministrator(administrator);

                session.commit();
                LOGGER.debug(className + ": Administrator = {} successfully updated", administrator);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.debug(className + ": Can't update administrator = {}", administrator, ex);

                throw ex;
            }
        }
    }

    @Override
    public void updateDoctorSchedule(Doctor doctor, List<ScheduleCell> newSchedule) {
        LOGGER.debug(className + ": Doctor = {} schedule = {} update", doctor, newSchedule);

        try (SqlSession session = getSession()) {
            try {
                getAdminMapper(session).updateDoctorSchedule(doctor, newSchedule);

                session.commit();
                LOGGER.debug(className + ": Doctor = {} schedule = {} successfully updated", doctor, newSchedule);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.debug(className + ": Can't update doctor = {} schedule = {}", doctor, newSchedule);

                throw ex;
            }
        }
    }

    @Override
    public void deleteDoctor(int id) {
        LOGGER.debug(className + ": Delete doctor with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).removeUser(id);

                session.commit();
                LOGGER.debug(className + ": Doctor with id = {} removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.debug(className + ": Can't remove doctor with id = {}", id, ex);

                throw ex;
            }
        }
    }
}