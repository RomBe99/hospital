package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.dao.DoctorDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DoctorDaoImpl extends BaseDaoImpl implements DoctorDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);
    private static final String className = DoctorDaoImpl.class.getSimpleName();

    @Override
    public Doctor insertDoctor(Doctor doctor) {
        LOGGER.debug(className + ": Insert doctor = {}", doctor);

        try (SqlSession session = getSession()) {
            try {
                int cabinetId = getCommonMapper(session).getCabinetIdByName(doctor.getCabinet());
                int specialityId = getCommonMapper(session).getDoctorSpecialityIdByName(doctor.getSpecialty());

                getUserMapper(session).insertUser(doctor);
                getDoctorMapper(session).insertDoctor(doctor.getId(), cabinetId, specialityId);

                session.commit();
                LOGGER.debug(className + ": Doctor = {} successfully inserted", doctor);

                return doctor;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't insert doctor = {}", doctor, ex);

                throw ex;
            }
        }
    }

    @Override
    public void removeDoctor(int id) {
        LOGGER.debug(className + ": Delete doctor with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                getDoctorMapper(session).removeDoctor(id);

                session.commit();
                LOGGER.debug(className + ": Doctor with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't remove doctor with id = {}", id, ex);

                throw ex;
            }
        }
    }

    @Override
    public List<Doctor> getAllDoctors() {
        LOGGER.debug(className + ": Get all doctors");

        try (SqlSession session = getSession()) {
            return session.selectList("net.thumbtack.hospital.mapper.DoctorMapper.getAllDoctors");
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't get all doctors", ex);

            throw ex;
        }
    }

    @Override
    public Doctor getDoctorById(int id) {
        LOGGER.debug(className + ": Get doctor by id");

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.DoctorMapper.getDoctorById", id);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't get doctor by id", ex);

            throw ex;
        }
    }
}