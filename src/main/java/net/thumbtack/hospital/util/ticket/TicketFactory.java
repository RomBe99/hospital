package net.thumbtack.hospital.util.ticket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ConfigurationProperties("hospital.ticket")
public class TicketFactory {
    @Value("#{${hospital.ticket.doctor-ticket-prefix}}")
    private String doctorTicketPrefix;
    @Value("#{${hospital.ticket.medical-commission-ticket-prefix}}")
    private String commissionTicketPrefix;

    public String buildTicketToDoctor(int doctorId, LocalDate date, LocalTime time) {
        return buildDoctorChapter(doctorId)
                .append(convertDateToString(date))
                .append(convertTimeToString(time))
                .toString();
    }

    public String buildTicketToCommission(LocalDate date, LocalTime time, List<Integer> doctorIds) {
        final var result = new StringBuilder(commissionTicketPrefix);

        doctorIds.forEach(i -> result.append(buildDoctorChapter(i)));

        return result
                .append(convertDateToString(date))
                .append(convertTimeToString(time))
                .toString();
    }

    private StringBuilder convertDateToString(LocalDate date) {
        return new StringBuilder()
                .append(date.getDayOfMonth())
                .append(date.getMonthValue())
                .append(date.getYear());
    }

    private StringBuilder convertTimeToString(LocalTime time) {
        return new StringBuilder()
                .append(time.getHour())
                .append(time.getMinute());
    }

    private StringBuilder buildDoctorChapter(int doctorId) {
        return new StringBuilder(doctorTicketPrefix)
                .append(doctorId);
    }
}