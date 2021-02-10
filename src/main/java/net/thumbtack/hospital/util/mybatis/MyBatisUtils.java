package net.thumbtack.hospital.util.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

public class MyBatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisUtils.class);
    private static final String CLASS_NAME = MyBatisUtils.class.getSimpleName();

    public static void initConnection() {
        if (sqlSessionFactory != null) {
            return;
        }

        LOGGER.debug(CLASS_NAME + ": Initializing SQL sessions factory");

        final String configResource = "mybatis-config.xml";

        try (Reader reader = Resources.getResourceAsReader(configResource)) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException ex) {
            LOGGER.error(CLASS_NAME + ": Can't initialize SQL session factory, error loading mybatis-config.xml", ex);

            throw new RuntimeException(CLASS_NAME + ": Can't create connection, stop", ex);
        }

        LOGGER.debug(CLASS_NAME + ": SQL sessions factory successfully Initialized");
    }

    public static SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }
}