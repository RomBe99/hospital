package net.thumbtack.hospital.util.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MyBatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisUtils.class);

    public static void initConnection() {
        if (sqlSessionFactory != null) {
            return;
        }

        LOGGER.debug("Initializing SQL sessions factory");

        final var configResource = "mybatis-config.xml";

        try (final var reader = Resources.getResourceAsReader(configResource)) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException ex) {
            LOGGER.error("Can't initialize SQL session factory, error loading mybatis-config.xml", ex);

            throw new RuntimeException("Can't create connection, stop", ex);
        }

        LOGGER.debug("SQL sessions factory successfully Initialized");
    }

    public static SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }
}