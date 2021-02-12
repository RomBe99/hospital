package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.PatientMapper;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.user.Patient;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Component("PatientDaoImpl")
public class PatientDaoImpl implements PatientDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDaoImpl.class);

    private final MapperFactory mapperFactory = new MapperFactory();

    @Override
    public void insertPatient(Patient patient) {
        LOGGER.debug("Insert patient = {}", patient);

        try (SqlSession session = getSession()) {
            try {
                final Integer userTypeId = mapperFactory.getMapper(session, CommonMapper.class).getUserTypeId(UserType.PATIENT.getType());

                PatientMapper mapper = mapperFactory.getMapper(session, PatientMapper.class);
                mapper.insertUser(patient, userTypeId);
                mapper.insertPatient(patient);

                session.commit();
                LOGGER.debug("Patient = {} successfully inserted", patient);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("Can't insert patient = {}", patient, ex);

                throw ex;
            }
        }
    }

    @Override
    public void updatePatient(Patient patient, String newPassword) {
        LOGGER.debug("Update patient = {}", patient);

        try (SqlSession session = getSession()) {
            try {
                final PatientMapper mapper = mapperFactory.getMapper(session, PatientMapper.class);
                mapper.updateUser(patient, newPassword);
                mapper.updatePatient(patient);

                session.commit();
                LOGGER.debug("Patient = {} successfully updated", patient);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("Can't update patient = {}", patient, ex);

                throw ex;
            }
        }
    }

    @Override
    public Patient getPatientById(int id) {
        LOGGER.debug("Get patient with id = {}", id);

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.PatientMapper.getPatientById", id);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get patient with id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public int hasPermissions(String sessionId) {
        LOGGER.debug("Checking patient permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            final Integer userId = mapperFactory.getMapper(session, PatientMapper.class).hasPermissions(sessionId);

            return userId == null ? 0 : userId;
        } catch (RuntimeException ex) {
            LOGGER.error("Can't check patient permissions for session id = {}", sessionId, ex);

            throw ex;
        }
    }
}
