package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.PatientDao;
import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.PatientMapper;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.model.user.Doctor;
import net.thumbtack.hospital.model.user.Patient;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCode;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Component("PatientDaoImpl")
public class PatientDaoImpl implements PatientDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDaoImpl.class);
    private static final String CLASS_NAME = PatientDaoImpl.class.getSimpleName();

    private final MapperFactory mapperFactory = new MapperFactory();
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public Patient insertPatient(Patient patient) {
        LOGGER.debug(CLASS_NAME + ": Insert patient = {}", patient);

        try (SqlSession session = getSession()) {
            try {
                int userTypeId = mapperFactory.getMapper(session, CommonMapper.class).getUserTypeId(UserType.PATIENT.getType());

                PatientMapper mapper = mapperFactory.getMapper(session, PatientMapper.class);
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
                PatientMapper mapper = mapperFactory.getMapper(session, PatientMapper.class);
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
                mapperFactory.getMapper(session, PatientMapper.class).removePatient(id);

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
                mapperFactory.getMapper(session, PatientMapper.class).appointmentToDoctor(patientId, doctorId, date, time);

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
                mapperFactory.getMapper(session, PatientMapper.class).denyMedicalCommission(ticket);

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
                mapperFactory.getMapper(session, PatientMapper.class).denyTicket(ticket);

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
    public int login(String sessionId, String login, String password) {
        return userDao.login(sessionId, login, password);
    }

    @Override
    public void logout(String sessionId) {
        userDao.logout(sessionId);
    }

    @Override
    public int hasPermissions(String sessionId) throws PermissionDeniedException {
        LOGGER.debug(CLASS_NAME + ": Checking patient permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return mapperFactory.getMapper(session, PatientMapper.class).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't check patient permissions for session id = {}", sessionId, ex);

            throw new PermissionDeniedException(PermissionDeniedErrorCode.PERMISSION_DENIED);
        }
    }

    @Override
    public Doctor getDoctorInformation(int patientId, int doctorId, LocalDate startDate, LocalDate endDate) {
        return userDao.getDoctorInformation(patientId, doctorId, startDate, endDate);
    }

    @Override
    public List<Doctor> getDoctorsInformation(int patientId, String speciality, LocalDate startDate, LocalDate endDate) {
        return userDao.getDoctorsInformation(patientId, speciality, startDate, endDate);
    }
}
