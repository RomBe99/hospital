package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.DoctorMapper;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Doctor;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Component("DoctorDaoImpl")
public class DoctorDaoImpl implements DoctorDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);
    private static final String CLASS_NAME = DoctorDaoImpl.class.getSimpleName();

    private final MapperFactory mapperFactory = new MapperFactory();

    @Override
    public void insertDoctor(Doctor doctor) {
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
    public int hasPermissions(String sessionId) {
        LOGGER.debug(CLASS_NAME + ": Checking doctor permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            Integer userId = mapperFactory.getMapper(session, DoctorMapper.class).hasPermissions(sessionId);

            return userId == null ? 0 : userId;
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't check doctor permissions for session id = {}", sessionId, ex);

            throw ex;
        }
    }
}