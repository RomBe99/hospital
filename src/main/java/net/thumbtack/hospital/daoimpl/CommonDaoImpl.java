package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.dao.CommonDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonDaoImpl extends BaseDaoImpl implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);
    private static final String className = CommonDaoImpl.class.getSimpleName();

    @Override
    public void clear() {
        LOGGER.debug(className + ": Delete all tables from database");

        try (SqlSession session = getSession()) {
            try {
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