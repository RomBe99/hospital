package net.thumbtack.hospital.server;

import net.thumbtack.hospital.util.error.ErrorMessageFactory;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import net.thumbtack.hospital.util.ticket.TicketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(scanBasePackages = "net.thumbtack.hospital")
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:errors.properties"),
        @PropertySource("classpath:tickets.properties"),
        @PropertySource("classpath:constraints.properties"),
        @PropertySource("classpath:database.properties")

})
@EnableConfigurationProperties({
        ErrorMessageFactory.class,
        TicketFactory.class
})
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