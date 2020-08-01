package net.thumbtack.hospital.util.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

public class MyBatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisUtils.class);
    private static boolean setUpIsDone = false;

    private static boolean initSqlSessionFactory() {
        LOGGER.debug("Initializing SQL sessions factory");

        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            LOGGER.debug("SQL sessions factory successfully Initialized");

// 		instead of setting here, it is possible to set in configuration XML file
//      sqlSessionFactory.getConfiguration().setAggressiveLazyLoading(false);

            return true;
        } catch (Exception e) {
            LOGGER.error("Can't initialize SQL session factory, error loading mybatis-config.xml", e);

            return false;
        }
    }

    public static SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }

    public static void initConnection() {
        if (!setUpIsDone) {
            boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();

            if (!initSqlSessionFactory) {
                throw new RuntimeException("Can't create connection, stop");
            }

            setUpIsDone = true;
        }
    }
}