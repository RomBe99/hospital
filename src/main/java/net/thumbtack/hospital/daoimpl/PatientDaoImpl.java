package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.model.Patient;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientDaoImpl extends BaseDaoImpl implements PatientDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDaoImpl.class);
    private static final String className = PatientDaoImpl.class.getSimpleName();

    @Override
    public Patient insertPatient(Patient patient) {
        LOGGER.debug(className + ": Insert patient = {}", patient);

        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).insertUser(patient);
                getPatientMapper(session).insertPatient(patient);

                session.commit();
                LOGGER.debug(className + ": Patient = {} successfully inserted", patient);

                return patient;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.debug(className + ": Can't insert patient = {}", patient, ex);

                throw ex;
            }
        }
    }

    @Override
    public void updatePatient(Patient patient) {
        LOGGER.debug(className + ": Update patient = {}", patient);

        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).updateUser(patient);
                getPatientMapper(session).updatePatient(patient);

                session.commit();
                LOGGER.debug(className + ": Patient = {} successfully updated", patient);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.debug(className + ": Can't update patient = {}", patient, ex);

                throw ex;
            }
        }
    }
}
