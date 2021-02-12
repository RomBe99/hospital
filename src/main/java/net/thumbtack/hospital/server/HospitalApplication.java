package net.thumbtack.hospital.server;

import net.thumbtack.hospital.util.error.ErrorMessageFactory;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "net.thumbtack.hospital")
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties(ErrorMessageFactory.class)
public class HospitalApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(HospitalApplication.class);

    public static void main(String[] args) {
        try {
            MyBatisUtils.initConnection();
        } catch (RuntimeException ex) {
            LOGGER.error("Can't connect to database", ex);
            LOGGER.info("application terminated");

            return;
        }

        SpringApplication.run(HospitalApplication.class);

        LOGGER.info("application is ready to use");
    }
}