package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.MedicalCommissionDao;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.MedicalCommissionMapper;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Component("MedicalCommissionImpl")
public class MedicalCommissionDaoImpl implements MedicalCommissionDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalCommissionDaoImpl.class);
    private static final String CLASS_NAME = MedicalCommissionDaoImpl.class.getSimpleName();

    private final MapperFactory mapperFactory = new MapperFactory();

    @Override
    public void createMedicalCommission(TicketToMedicalCommission ticket) {
        LOGGER.debug(CLASS_NAME + ": Creating medical commission = {}", ticket);

        try (SqlSession session = getSession()) {
            try {
                MedicalCommissionMapper mapper = mapperFactory.getMapper(session, MedicalCommissionMapper.class);
                mapper.createMedicalCommission(ticket);
                mapper.insertDoctorsInMedicalCommission(ticket.getTitle(), ticket.getDoctorIds());

                LOGGER.debug(CLASS_NAME + ": Medical commission = {} successfully created", ticket);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't create medical commission = {}", ticket, ex);

                throw ex;
            }
        }
    }

    @Override
    public void denyMedicalCommission(String ticketTitle) {
        LOGGER.debug(CLASS_NAME + ": Deny ticket with title = {} to commission", ticketTitle);

        try (SqlSession session = getSession()) {
            try {
                mapperFactory.getMapper(session, MedicalCommissionMapper.class).denyMedicalCommission(ticketTitle);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Successfully deny ticket with title = {} to commission", ticketTitle);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't deny ticket with title = {} to commission", ticketTitle, ex);

                throw ex;
            }
        }
    }

    @Override
    public List<TicketToMedicalCommission> getTicketsToMedicalCommission(int patientId) {
        LOGGER.debug(CLASS_NAME + ": Get all tickets to medical commission for patient = {}", patientId);

        try (SqlSession session = getSession()) {
            return session.selectList("net.thumbtack.hospital.mapper.PatientMapper.getTicketsToMedicalCommission", patientId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get all tickets to medical commission for patient = {}", patientId, ex);

            throw ex;
        }
    }
}