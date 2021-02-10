package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.ScheduleDao;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.ScheduleMapper;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Component("ScheduleDaoImpl")
public class ScheduleDaoImpl implements ScheduleDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDaoImpl.class);
    private static final String CLASS_NAME = ScheduleDaoImpl.class.getSimpleName();

    private final MapperFactory mapperFactory = new MapperFactory();

    @Override
    public void insertSchedule(int doctorId, List<ScheduleCell> schedule) {
        LOGGER.debug(CLASS_NAME + ": Insert schedule = {}", schedule);

        try (SqlSession session = getSession()) {
            try {
                final ScheduleMapper mapper = mapperFactory.getMapper(session, ScheduleMapper.class);
                mapper.insertScheduleCells(doctorId, schedule);

                for (ScheduleCell c : schedule) {
                    mapper.insertTimeCells(c.getId(), c.getCells());
                }

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Schedule = {} successfully inserted", schedule);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't insert schedule = {}", schedule, ex);

                throw ex;
            }
        }
    }

    @Override
    public void editSchedule(int doctorId, LocalDate dateStart, LocalDate dateEnd, List<ScheduleCell> newSchedule) {
        LOGGER.debug(CLASS_NAME + ": Edit schedule from date start = {} and date end = {} where schedule = {}",
                dateStart, dateEnd, newSchedule);

        try (SqlSession session = getSession()) {
            try {
                final ScheduleMapper mapper = mapperFactory.getMapper(session, ScheduleMapper.class);
                mapper.removeSchedule(doctorId, dateStart, dateEnd);
                mapper.insertScheduleCells(doctorId, newSchedule);

                for (ScheduleCell c : newSchedule) {
                    mapper.insertTimeCells(c.getId(), c.getCells());
                }

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Schedule from date start = {} and date end = {} where schedule = {} successfully edited",
                        dateStart, dateEnd, newSchedule);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't edit schedule from date start = {} and date end = {} where schedule = {}",
                        dateStart, dateEnd, newSchedule, ex);

                throw ex;
            }
        }
    }

    @Override
    public void appointmentToDoctor(int patientId, String ticketTitle) {
        LOGGER.debug(CLASS_NAME + ": Patient = {} appointment to ticket = {}", patientId, ticketTitle);

        try (SqlSession session = getSession()) {
            try {
                mapperFactory.getMapper(session, ScheduleMapper.class).appointmentToDoctor(patientId, ticketTitle);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Patient = {} successfully appointment to ticket = {}", patientId, ticketTitle);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Patient = {} can't appointment to ticket = {}", patientId, ticketTitle, ex);

                throw ex;
            }
        }
    }

    @Override
    public void denyTicket(int patientId, String ticketTitle) {
        LOGGER.debug(CLASS_NAME + ": Patient = {} deny ticket = {}", patientId, ticketTitle);

        try (SqlSession session = getSession()) {
            try {
                mapperFactory.getMapper(session, ScheduleMapper.class).denyTicket(patientId, ticketTitle);

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Patient = {} successfully deny ticket = {}", patientId, ticketTitle);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Patient = {} can't deny ticket = {}", patientId, ticketTitle, ex);

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
            LOGGER.error(CLASS_NAME + ": Can't get all tickets to doctor for patient = {}", patientId, ex);

            throw ex;
        }
    }
}
