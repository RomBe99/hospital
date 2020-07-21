package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.mapper.PatientMapper;
import net.thumbtack.hospital.mapper.UserTypes;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCodes;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component("PatientDaoImpl")
public class PatientDaoImpl extends UserDaoImpl implements PatientDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDaoImpl.class);
    private static final String CLASS_NAME = PatientDaoImpl.class.getSimpleName();

    private PatientMapper getPatientMapper(SqlSession session) {
        return session.getMapper(PatientMapper.class);
    }

    @Override
    public Patient insertPatient(Patient patient) {
        LOGGER.debug(CLASS_NAME + ": Insert patient = {}", patient);

        try (SqlSession session = getSession()) {
            try {
                int userTypeId = getCommonMapper(session).getUserTypeId(UserTypes.PATIENT.getType());

                PatientMapper mapper = getPatientMapper(session);
                mapper.insertUser(patient, userTypeId);
                mapper.insertPatient(patient);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Patient = {} successfully inserted", patient);

                return patient;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't insert patient = {}", patient, ex);

                throw ex;
            }
        }
    }

    @Override
    public void updatePatient(Patient patient) {
        LOGGER.debug(CLASS_NAME + ": Update patient = {}", patient);

        try (SqlSession session = getSession()) {
            try {
                PatientMapper mapper = getPatientMapper(session);
                mapper.updateUser(patient);
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
    public void removePatient(int id) {
        LOGGER.debug(CLASS_NAME + ": Remove patient with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                getPatientMapper(session).removePatient(id);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Patient with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't remove patient with id = {}", id, ex);

                throw ex;
            }
        }
    }

    @Override
    public void appointmentToDoctor(int patientId, int doctorId, LocalDate date, LocalTime time) {
        LOGGER.debug(CLASS_NAME + ": Patient = {} appointment to doctor = {} on date = {}, time = {}",
                patientId, doctorId, date, time);

        try (SqlSession session = getSession()) {
            try {
                getPatientMapper(session).appointmentToDoctor(patientId, doctorId, date, time);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Patient = {} successfully appointment to doctor = {} on date = {}, time = {}",
                        patientId, doctorId, date, time);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Patient = {} can't appointment to doctor = {} on date = {}, time = {}",
                        patientId, doctorId, date, time);

                throw ex;
            }
        }
    }

    @Override
    public void denyMedicalCommission(String ticket) {
        LOGGER.debug(CLASS_NAME + ": Deny ticket = {} to commission", ticket);

        try (SqlSession session = getSession()) {
            try {
                getPatientMapper(session).denyMedicalCommission(ticket);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Successfully deny ticket = {} to commission", ticket);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't deny ticket = {} to commission", ticket, ex);

                throw ex;
            }
        }
    }

    @Override
    public void denyTicket(String ticket) {
        LOGGER.debug(CLASS_NAME + ": Deny ticket = {} ", ticket);

        try (SqlSession session = getSession()) {
            try {
                getPatientMapper(session).denyTicket(ticket);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Successfully deny ticket = {}", ticket);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't deny ticket = {}", ticket, ex);

                throw ex;
            }
        }
    }

    @Override
    public List<TicketToDoctor> getTicketsToDoctor(int patientId) {
        LOGGER.debug(CLASS_NAME + ": Get all tickets to doctor for patient = {}", patientId);

        try (SqlSession session = getSession()) {
            return session.selectList("net.thumbtack.hospital.mapper.PatientMapper.getTicketsToDoctor", patientId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get all tickets to doctor for patient = {}", patientId);

            throw ex;
        }
    }

    @Override
    public List<TicketToMedicalCommission> getTicketsToMedicalCommission(int patientId) {
        LOGGER.debug(CLASS_NAME + ": Get all tickets to medical commission for patient = {}", patientId);

        try (SqlSession session = getSession()) {
            return session.selectList("net.thumbtack.hospital.mapper.PatientMapper.getTicketsToMedicalCommission", patientId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get all tickets to medical commission for patient = {}", patientId);

            throw ex;
        }
    }

    @Override
    public int hasPermissions(String sessionId) throws PermissionDeniedException {
        LOGGER.debug(CLASS_NAME + ": Checking patient permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return getPatientMapper(session).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't check patient permissions for session id = {}", sessionId, ex);

            throw new PermissionDeniedException(PermissionDeniedErrorCodes.PERMISSION_DENIED);
        }
    }
}
