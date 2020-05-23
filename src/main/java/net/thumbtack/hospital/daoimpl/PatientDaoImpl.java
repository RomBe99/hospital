package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.mapper.PatientMapper;
import net.thumbtack.hospital.model.Patient;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCodes;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component("PatientDaoImpl")
public class PatientDaoImpl extends UserDaoImpl implements PatientDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDaoImpl.class);
    private static final String className = PatientDaoImpl.class.getSimpleName();

    private PatientMapper getPatientMapper(SqlSession session) {
        return session.getMapper(PatientMapper.class);
    }

    @Override
    public Patient insertPatient(Patient patient) {
        LOGGER.debug(className + ": Insert patient = {}", patient);

        try (SqlSession session = getSession()) {
            try {
                PatientMapper mapper = getPatientMapper(session);
                mapper.insertUser(patient);
                mapper.insertPatient(patient);

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

        try (SqlSession session = getSession()) {
            try {
                PatientMapper mapper = getPatientMapper(session);
                mapper.updateUser(patient);
                mapper.updatePatient(patient);

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

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.PatientMapper.getPatientById", id);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't get patient with id = {}", id, ex);

            throw ex;
        }
    }

    @Override
    public void removePatient(int id) {
        LOGGER.debug(className + ": Remove patient with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                getPatientMapper(session).removePatient(id);

                session.commit();
                LOGGER.debug(className + ": Patient with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't remove patient with id = {}", id, ex);

                throw ex;
            }
        }
    }

    @Override
    public void appointmentToDoctor(int patientId, int doctorId, LocalDate date, LocalTime time) {
        LOGGER.debug(className + ": Patient = {} appointment to doctor = {} on date = {}, time = {}",
                patientId, doctorId, date, time);

        try (SqlSession session = getSession()) {
            try {
                getPatientMapper(session).appointmentToDoctor(patientId, doctorId, date, time);

                session.commit();
                LOGGER.debug(className + ": Patient = {} successfully appointment to doctor = {} on date = {}, time = {}",
                        patientId, doctorId, date, time);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Patient = {} can't appointment to doctor = {} on date = {}, time = {}",
                        patientId, doctorId, date, time);

                throw ex;
            }
        }
    }

    @Override
    public void denyMedicalCommission(int patientId, int commissionTicketId) {
        LOGGER.debug(className + ": Patient = {} deny ticket to commission = {}", patientId, commissionTicketId);

        try (SqlSession session = getSession()) {
            try {
                getPatientMapper(session).denyMedicalCommission(patientId, commissionTicketId);

                session.commit();
                LOGGER.debug(className + ": Patient = {} successfully deny ticket to commission = {}", patientId, commissionTicketId);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Patient = {} can't deny ticket to commission = {}", patientId, commissionTicketId, ex);

                throw ex;
            }
        }
    }

    @Override
    public void denyTicket(int patientId, int doctorId, LocalDate date, LocalTime time) {
        LOGGER.debug(className + ": Patient = {} deny ticket to doctor = {} appointment on date = {} and time = {}",
                patientId, doctorId, date, time);

        try (SqlSession session = getSession()) {
            try {
                getPatientMapper(session).denyTicket(patientId, doctorId, date, time);

                session.commit();
                LOGGER.debug(className + ": Patient = {} successfully deny ticket to doctor = {} appointment on date = {} and time = {}",
                        patientId, doctorId, date, time);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Patient = {} can't deny ticket to doctor = {} appointment on date = {} and time = {}",
                        patientId, doctorId, date, time, ex);

                throw ex;
            }
        }
    }

    @Override
    public int hasPermissions(String sessionId) throws PermissionDeniedException {
        LOGGER.debug(className + ": Checking patient permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return getPatientMapper(session).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't check patient permissions for session id = {}", sessionId, ex);

            throw new PermissionDeniedException(PermissionDeniedErrorCodes.PERMISSION_DENIED);
        }
    }
}
