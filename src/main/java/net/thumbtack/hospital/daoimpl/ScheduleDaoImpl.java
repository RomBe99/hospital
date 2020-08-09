package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.ScheduleDao;
import net.thumbtack.hospital.mapper.MapperFactory;
import net.thumbtack.hospital.mapper.ScheduleMapper;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
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
                ScheduleMapper mapper = mapperFactory.getMapper(session, ScheduleMapper.class);
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
    public void editSchedule(int doctorId, LocalDate dateStart, LocalDate dateEnd, List<ScheduleCell> schedule) {
        LOGGER.debug(CLASS_NAME + ": Edit schedule from date start = {} and date end = {} where schedule = {}",
                dateStart, dateEnd, schedule);

        try (SqlSession session = getSession()) {
            try {
                ScheduleMapper mapper = mapperFactory.getMapper(session, ScheduleMapper.class);
                mapper.removeSchedule(doctorId, dateStart, dateEnd);
                mapper.insertScheduleCells(doctorId, schedule);

                for (ScheduleCell c : schedule) {
                    mapper.insertTimeCells(c.getId(), c.getCells());
                }

                session.commit();
                LOGGER.debug(CLASS_NAME + ": Schedule from date start = {} and date end = {} where schedule = {} successfully edited",
                        dateStart, dateEnd, schedule);
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(CLASS_NAME + ": Can't edit schedule from date start = {} and date end = {} where schedule = {}",
                        dateStart, dateEnd, schedule, ex);

                throw ex;
            }
        }
    }
}
