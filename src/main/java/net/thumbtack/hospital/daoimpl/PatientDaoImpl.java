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
    private static final String CLASS_NAME = PatientDaoImpl.class.getSimpleName();

    private final MapperFactory mapperFactory = new MapperFactory();

    @Override
    public void insertPatient(Patient patient) {
        LOGGER.debug(CLASS_NAME + ": Insert patient = {}", patient);

        try (SqlSession session = getSession()) {
            try {
                int userTypeId = mapperFactory.getMapper(session, CommonMapper.class).getUserTypeId(UserType.PATIENT.getType());

                PatientMapper mapper = mapperFactory.getMapper(session, PatientMapper.class);
                mapper.insertUser(patient, userTypeId);
                mapper.insertPatient(patient);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Patient = {} successfully inserted", patient);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't insert patient = {}", patient, ex);

                throw ex;
            }
        }
    }

    @Override
    public void updatePatient(Patient patient, String newPassword) {
        LOGGER.debug(CLASS_NAME + ": Update patient = {}", patient);

        try (SqlSession session = getSession()) {
            try {
                PatientMapper mapper = mapperFactory.getMapper(session, PatientMapper.class);
                mapper.updateUser(patient, newPassword);
                mapper.updatePatient(patient);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Patient = {} successfully updated", patient);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't update patient = {}", patient, ex);

                throw ex;
            }
        }
    }

    @Override
    public Patient getPatientById(int id) {
        LOGGER.debug(CLASS_NAME + ": Get patient with id = {}", id);

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.PatientMapper.getPatientById", id);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get patient with id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public int hasPermissions(String sessionId) {
        LOGGER.debug(CLASS_NAME + ": Checking patient permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            Integer userId = mapperFactory.getMapper(session, PatientMapper.class).hasPermissions(sessionId);

            return userId == null ? 0 : userId;
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't check patient permissions for session id = {}", sessionId, ex);

            throw ex;
        }
    }
}
