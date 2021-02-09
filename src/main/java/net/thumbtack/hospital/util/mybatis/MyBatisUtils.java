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

    public static void initConnection() {
        if (sqlSessionFactory == null) {
            LOGGER.debug("Initializing SQL sessions factory");

            try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                LOGGER.debug("SQL sessions factory successfully Initialized");
            } catch (IOException e) {
                LOGGER.error("Can't initialize SQL session factory, error loading mybatis-config.xml", e);

                throw new RuntimeException("Can't create connection, stop", e);
            }
        }
    }

    public static SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }
}