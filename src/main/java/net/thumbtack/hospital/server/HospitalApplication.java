package net.thumbtack.hospital.server;

import net.thumbtack.hospital.daoimpl.AdminDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HospitalApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDaoImpl.class);

    public static void main(String[] args) {
        LOGGER.info(HospitalApplication.class.getSimpleName() + " is running");
        SpringApplication.run(HospitalApplication.class);
        LOGGER.info(HospitalApplication.class.getSimpleName() + " application terminated");
    }
}