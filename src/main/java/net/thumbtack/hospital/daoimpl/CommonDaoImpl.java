package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.CommonDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonDaoImpl extends BaseDaoImpl implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);
    private static final String className = CommonDaoImpl.class.getSimpleName();

    @Override
    public int getDoctorSpecialityIdByName(String name) {
        LOGGER.debug(className + ": Get doctor speciality id with name  = {}", name);

        try (SqlSession session = getSession()) {
            return getCommonMapper(session).getDoctorSpecialityIdByName(name);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Get doctor speciality id with name  = {}", name, ex);

            throw ex;
        }
    }

    @Override
    public int getCabinetIdByName(String name) {
        LOGGER.debug(className + ": Get cabinet id with name  = {}", name);

        try (SqlSession session = getSession()) {
            return getCommonMapper(session).getCabinetIdByName(name);
        } catch (RuntimeException ex) {
            LOGGER.error(className + ": Get cabinet id with name  = {}", name, ex);

            throw ex;
        }
    }

    @Override
    public void clear() {
        LOGGER.debug(className + ": Delete all tables from database");

        try (SqlSession session = getSession()) {
            try {
                getDoctorMapper(session).deleteAll();
                getAdminMapper(session).deleteAll();
                getPatientMapper(session).deleteAll();
                getUserMapper(session).deleteAll();

                session.commit();
                LOGGER.debug(className + ": All tables deleted from database");
            } catch (RuntimeException ex) {
                session.rollback();
                LOGGER.error(className + ": Can't clear database", ex);

                throw ex;
            }
        }
    }
}