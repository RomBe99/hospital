package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.MedicalCommissionDao;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.MedicalCommissionMapper;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
                mapper.insertDoctorsInMedicalCommission(ticket.getTicket(), ticket.getDoctorIds());

                LOGGER.debug(CLASS_NAME + ": Medical commission = {} successfully created", ticket);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't create medical commission = {}", ticket, ex);

                throw ex;
            }
        }
    }
}