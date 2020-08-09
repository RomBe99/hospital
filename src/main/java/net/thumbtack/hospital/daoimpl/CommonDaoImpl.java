package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.CommonDao;
import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.mapper.MapperFactory;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static net.thumbtack.hospital.util.mybatis.MyBatisUtils.getSession;

@Component("CommonDaoImpl")
public class CommonDaoImpl implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);
    private static final String CLASS_NAME = CommonDaoImpl.class.getSimpleName();

    private final MapperFactory mapperFactory;

    @Autowired
    public CommonDaoImpl(MapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
    }

    @Override
    public String getUserTypeByUserId(int userId) {
        LOGGER.debug(CLASS_NAME + ": Get user type by user id = {}", userId);

        try (SqlSession session = getSession()) {
            return mapperFactory.getMapper(session, CommonMapper.class).getUserTypeByUserId(userId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get user type by user id  = {}", userId, ex);

            throw ex;
        }
    }

    @Override
    public boolean containsAppointment(int doctorId, LocalDate dateStart, LocalDate dateEnd) {
        LOGGER.debug(CLASS_NAME + ": Check is an appointment to doctor = {} from {} to {}", doctorId, dateStart, dateEnd);

        try (SqlSession session = getSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("doctorId", doctorId);
            params.put("dateStart", dateStart);
            params.put("dateEnd", dateEnd);

            return session.selectOne("net.thumbtack.hospital.mapper.CommonMapper.containsAppointment", params);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't check is an appointment to doctor = {} from {} to {}",
                    doctorId, dateStart, dateEnd, ex);

            throw ex;
        }
    }
}