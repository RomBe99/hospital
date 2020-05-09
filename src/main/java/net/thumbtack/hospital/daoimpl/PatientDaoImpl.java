package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.model.Patient;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PatientDaoImpl extends BaseDaoImpl implements PatientDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDaoImpl.class);
    private static final String className = PatientDaoImpl.class.getSimpleName();

    @Override
    public Patient insertPatient(Patient patient) {
        LOGGER.debug(className + ": Insert patient = {}", patient);

        try (SqlSession session = super.getSession()) {
            try {
                super.getUserMapper(session).insertUser(patient);
                super.getPatientMapper(session).insertPatient(patient);

                session.commit();
                LOGGER.debug(className + ": Patient = {} successfully inserted", patient);

                return patient;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't insert patient = {}", patient, ex);

                throw ex;
            }
        }
    }

    @Override
    public void updatePatient(Patient patient) {
        LOGGER.debug(className + ": Update patient = {}", patient);

        try (SqlSession session = super.getSession()) {
            try {
                super.getUserMapper(session).updateUser(patient);
                super.getPatientMapper(session).updatePatient(patient);

                session.commit();
                LOGGER.debug(className + ": Patient = {} successfully updated", patient);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't update patient = {}", patient, ex);

                throw ex;
            }
        }
    }

    @Override
    public Patient getPatientById(int id) {
        LOGGER.debug(className + ": Get patient with id = {}", id);

        try (SqlSession session = super.getSession()) {
            return session.selectOne("net.thumbtack.hospital.mappers.PatientMapper.getPatientById", id);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't get patient with id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public void removePatient(int id) {
        LOGGER.debug(className + ": Remove patient with id = {}", id);

        try (SqlSession session = super.getSession()) {
            try {
                super.getPatientMapper(session).removePatient(id);

                session.commit();
                LOGGER.debug(className + ": Patient with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't remove patient with id = {}", id, ex);

                throw ex;
            }
        }
    }
}
