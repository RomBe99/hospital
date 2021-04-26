package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.MedicalCommissionDao;
import net.thumbtack.hospital.mapper.MedicalCommissionMapper;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Repository("MedicalCommissionImpl")
public class MedicalCommissionDaoImpl implements MedicalCommissionDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalCommissionDaoImpl.class);

    @Override
    public void createMedicalCommission(TicketToMedicalCommission ticket) {
        LOGGER.debug("Creating medical commission = {}", ticket);

        try (final var session = getSession()) {
            try {
                final var mapper = session.getMapper(MedicalCommissionMapper.class);
                mapper.createMedicalCommission(ticket);
                mapper.insertDoctorsInMedicalCommission(ticket.getId(), ticket.getDoctorIds());

                session.commit();
                LOGGER.debug("Medical commission = {} successfully created", ticket);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("Can't create medical commission = {}", ticket, ex);

                throw ex;
            }
        }
    }

    @Override
    public void denyMedicalCommission(String ticketTitle) {
        LOGGER.debug("Deny ticket with title = {} to commission", ticketTitle);

        try (final var session = getSession()) {
            try {
                session.getMapper(MedicalCommissionMapper.class).denyMedicalCommission(ticketTitle);

                session.commit();
                LOGGER.debug("Successfully deny ticket with title = {} to commission", ticketTitle);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error("Can't deny ticket with title = {} to commission", ticketTitle, ex);

                throw ex;
            }
        }
    }

    @Override
    public List<TicketToMedicalCommission> getTicketsToMedicalCommission(int patientId) {
        LOGGER.debug("Get all tickets to medical commission for patient = {}", patientId);

        try (final var session = getSession()) {
            return session.selectList("net.thumbtack.hospital.mapper.PatientMapper.getTicketsToMedicalCommission", patientId);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get all tickets to medical commission for patient = {}", patientId, ex);

            throw ex;
        }
    }
}