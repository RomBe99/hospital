package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.CommonDao;
import net.thumbtack.hospital.mapper.CommonMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Repository("CommonDaoImpl")
public class CommonDaoImpl implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);

    @Override
    public String getUserTypeByUserId(int userId) {
        LOGGER.debug("Get user type by user id = {}", userId);

        try (SqlSession session = getSession()) {
            return session.getMapper(CommonMapper.class).getUserTypeByUserId(userId);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't get user type by user id  = {}", userId, ex);

            throw ex;
        }
    }

    @Override
    public boolean containsAppointments(int doctorId, LocalDate dateStart, LocalDate dateEnd) {
        LOGGER.debug("Check is an appointment to doctor = {} from {} to {}", doctorId, dateStart, dateEnd);

        try (SqlSession session = getSession()) {
            final Map<String, Object> params = new HashMap<>();
            params.put("doctorId", doctorId);
            params.put("dateStart", dateStart);
            params.put("dateEnd", dateEnd);

            return session.selectOne("net.thumbtack.hospital.mapper.CommonMapper.containsAppointments", params);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't check is an appointment to doctor = {} from {} to {}",
                    doctorId, dateStart, dateEnd, ex);

            throw ex;
        }
    }

    @Override
    public boolean containsAppointment(String ticketTitle) {
        LOGGER.debug("Check is an appointment to ticket = {}", ticketTitle);

        try (SqlSession session = getSession()) {
            return session.getMapper(CommonMapper.class).containsAppointment(ticketTitle);
        } catch (RuntimeException ex) {
            LOGGER.error("Can't check appointment to ticket = {}", ticketTitle, ex);

            throw ex;
        }
    }
}