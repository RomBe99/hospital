package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.DoctorMapper;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Doctor;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Repository("DoctorDaoImpl")
public class DoctorDaoImpl implements DoctorDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);

    @Override
    public void insertDoctor(Doctor doctor) {
        LOGGER.debug("Insert doctor = {}", doctor);

        try (final var session = getSession()) {
            try {
                final var commonMapper = session.getMapper(CommonMapper.class);
                final var userTypeId = commonMapper.getUserTypeId(UserType.DOCTOR.getType());
                final var cabinetId = commonMapper.getCabinetIdByName(doctor.getCabinet());
                final var specialityId = commonMapper.getDoctorSpecialityIdByName(doctor.getSpecialty());

                var doctorMapper = session.getMapper(DoctorMapper.class);
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

        try (final var session = getSession()) {
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

        try (final var session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.DoctorMapper.getDoctorById", id);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get doctor by id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public Doctor getRandomDoctorBySpeciality(String speciality) {
        LOGGER.debug("Get random doctor by speciality = {}", speciality);

        try (final var session = getSession()) {
            final var specialityId = session.getMapper(CommonMapper.class).getDoctorSpecialityIdByName(speciality);

            final var doctors = session.<Doctor>selectList("net.thumbtack.hospital.mapper.DoctorMapper.getDoctorsBySpecialityId", specialityId);

            if (doctors.isEmpty()) {
                return null;
            }

            return doctors.get(RandomUtils.nextInt(0, doctors.size()));
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get random doctor by speciality = {}", speciality, ex);

            throw ex;
        }
    }

    @Override
    public int hasPermissions(String sessionId) {
        LOGGER.debug("Checking doctor permissions for session id = {}", sessionId);

        try (final var session = getSession()) {
            final var userId = session.getMapper(DoctorMapper.class).hasPermissions(sessionId);

            return userId == null ? 0 : userId;
        } catch (RuntimeException ex) {
            LOGGER.error("Can't check doctor permissions for session id = {}", sessionId, ex);

            throw ex;
        }
    }
}