package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.DoctorMapper;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Doctor;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Repository("DoctorDaoImpl")
public class DoctorDaoImpl implements DoctorDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);

    @Override
    public void insertDoctor(Doctor doctor) {
        LOGGER.debug("Insert doctor = {}", doctor);

        try (SqlSession session = getSession()) {
            try {
                final CommonMapper commonMapper = session.getMapper(CommonMapper.class);
                final Integer userTypeId = commonMapper.getUserTypeId(UserType.DOCTOR.getType());
                final Integer cabinetId = commonMapper.getCabinetIdByName(doctor.getCabinet());
                final Integer specialityId = commonMapper.getDoctorSpecialityIdByName(doctor.getSpecialty());

                DoctorMapper doctorMapper = session.getMapper(DoctorMapper.class);
                doctorMapper.insertUser(doctor, userTypeId);
                doctorMapper.insertDoctor(doctor.getId(), specialityId, cabinetId);

                session.commit();
                LOGGER.debug("Doctor = {} successfully inserted", doctor);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("Can't insert doctor = {}", doctor, ex);

                throw ex;
            }
        }
    }

    @Override
    public void removeDoctor(int id) {
        LOGGER.debug("Delete doctor with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                session.getMapper(DoctorMapper.class).removeDoctor(id);

                session.commit();
                LOGGER.debug("Doctor with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("Can't remove doctor with id = {}", id, ex);

                throw ex;
            }
        }
    }

    @Override
    public Doctor getDoctorById(int id) {
        LOGGER.debug("Get doctor by id = {}", id);

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.DoctorMapper.getDoctorById", id);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get doctor by id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public Doctor getRandomDoctorBySpeciality(String speciality) {
        LOGGER.debug("Get random doctor by speciality = {}", speciality);

        try (SqlSession session = getSession()) {
            final Integer specialityId = session.getMapper(CommonMapper.class).getDoctorSpecialityIdByName(speciality);

            final List<Doctor> doctors = session.selectList("net.thumbtack.hospital.mapper.DoctorMapper.getDoctorsBySpecialityId", specialityId);

            if (doctors.isEmpty()) {
                return null;
            }

            int index = (int) (Math.random() * doctors.size());

            return doctors.get(index);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get random doctor by speciality = {}", speciality, ex);

            throw ex;
        }
    }

    @Override
    public int hasPermissions(String sessionId) {
        LOGGER.debug("Checking doctor permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            final Integer userId = session.getMapper(DoctorMapper.class).hasPermissions(sessionId);

            return userId == null ? 0 : userId;
        } catch (RuntimeException ex) {
            LOGGER.error("Can't check doctor permissions for session id = {}", sessionId, ex);

            throw ex;
        }
    }
}