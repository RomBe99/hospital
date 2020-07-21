package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.CommonDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("CommonDaoImpl")
public class CommonDaoImpl extends BaseDaoImpl implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);
    private static final String CLASS_NAME = CommonDaoImpl.class.getSimpleName();

    @Override
    public int getDoctorSpecialityIdByName(String name) {
        LOGGER.debug(CLASS_NAME + ": Get doctor speciality id with name  = {}", name);

        try (SqlSession session = getSession()) {
            return getCommonMapper(session).getDoctorSpecialityIdByName(name);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get doctor speciality id with name  = {}", name, ex);

            throw ex;
        }
    }

    @Override
    public int getCabinetIdByName(String name) {
        LOGGER.debug(CLASS_NAME + ": Get cabinet id with name  = {}", name);

        try (SqlSession session = getSession()) {
            return getCommonMapper(session).getCabinetIdByName(name);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get cabinet id with name  = {}", name, ex);

            throw ex;
        }
    }

    @Override
    public String getUserTypeByUserId(int userId) {
        LOGGER.debug(CLASS_NAME + ": Get user type by user id = {}", userId);

        try (SqlSession session = getSession()) {
            return getCommonMapper(session).getUserTypeByUserId(userId);
        } catch (RuntimeException ex) {
            LOGGER.error(CLASS_NAME + ": Can't get user type by user id  = {}", userId, ex);

            throw ex;
        }
    }
}