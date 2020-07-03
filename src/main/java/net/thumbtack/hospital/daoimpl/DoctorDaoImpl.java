package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.DoctorDao;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.DoctorMapper;
import net.thumbtack.hospital.mapper.UserTypes;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCodes;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("DoctorDaoImpl")
public class DoctorDaoImpl extends UserDaoImpl implements DoctorDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDaoImpl.class);
    private static final String className = DoctorDaoImpl.class.getSimpleName();

    private DoctorMapper getDoctorMapper(SqlSession session) {
        return session.getMapper(DoctorMapper.class);
    }

    @Override
    public Doctor insertDoctor(Doctor doctor) {
        LOGGER.debug(className + ": Insert doctor = {}", doctor);

        try (SqlSession session = getSession()) {
            try {
                CommonMapper commonMapper = getCommonMapper(session);
                int userTypeId = getCommonMapper(session).getUserTypeId(UserTypes.DOCTOR.getType());
                int cabinetId = commonMapper.getCabinetIdByName(doctor.getCabinet());
                int specialityId = commonMapper.getDoctorSpecialityIdByName(doctor.getSpecialty());

                DoctorMapper doctorMapper = getDoctorMapper(session);
                doctorMapper.insertUser(doctor, userTypeId);
                doctorMapper.insertDoctor(doctor.getId(), specialityId, cabinetId);

                session.commit();
                LOGGER.debug(className + ": Doctor = {} successfully inserted", doctor);

                return doctor;
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't insert doctor = {}", doctor, ex);

                throw ex;
            }
        }
    }

    @Override
    public void removeDoctor(int id) {
        LOGGER.debug(className + ": Delete doctor with id = {}", id);

        try (SqlSession session = getSession()) {
            try {
                getDoctorMapper(session).removeDoctor(id);

                session.commit();
                LOGGER.debug(className + ": Doctor with id = {} successfully removed", id);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't remove doctor with id = {}", id, ex);

                throw ex;
            }
        }
    }

    @Override
    public List<Doctor> getAllDoctors() {
        LOGGER.debug(className + ": Get all doctors");

        try (SqlSession session = getSession()) {
            return session.selectList("net.thumbtack.hospital.mapper.DoctorMapper.getAllDoctors");
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't get all doctors", ex);

            throw ex;
        }
    }

    @Override
    public Doctor getDoctorById(int id) {
        LOGGER.debug(className + ": Get doctor by id");

        try (SqlSession session = getSession()) {
            return session.selectOne("net.thumbtack.hospital.mapper.DoctorMapper.getDoctorById", id);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't get doctor by id", ex);

            throw ex;
        }
    }

    @Override
    public void createMedicalCommission(TicketToMedicalCommission ticketToMedicalCommission) {
        LOGGER.debug(className + ": Creating medical commission = {}", ticketToMedicalCommission);

        try (SqlSession session = getSession()) {
            try {
                DoctorMapper mapper = getDoctorMapper(session);
                mapper.createMedicalCommission(ticketToMedicalCommission);

                for (int id : ticketToMedicalCommission.getDoctorIds()) {
                    mapper.insertDoctorInMedicalCommission(ticketToMedicalCommission.getTicket(), id);
                }

                LOGGER.debug(className + ": Medical commission = {} successfully created", ticketToMedicalCommission);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't create medical commission = {}", ticketToMedicalCommission, ex);

                throw ex;
            }
        }
    }

    @Override
    public int hasPermissions(String sessionId) throws PermissionDeniedException {
        LOGGER.debug(className + ": Checking doctor permissions for session id = {}", sessionId);

        try (SqlSession session = getSession()) {
            return getDoctorMapper(session).hasPermissions(sessionId);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Can't check doctor permissions for session id = {}", sessionId, ex);

            throw new PermissionDeniedException(PermissionDeniedErrorCodes.PERMISSION_DENIED);
        }
    }
}