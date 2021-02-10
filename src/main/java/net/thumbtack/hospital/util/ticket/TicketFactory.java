package net.thumbtack.hospital.util.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TicketFactory {
    private static StringBuilder convertDateToString(LocalDate date) {
        return new StringBuilder().append(date.getDayOfMonth()).append(date.getMonthValue()).append(date.getYear());
    }

    private static StringBuilder convertTimeToString(LocalTime time) {
        return new StringBuilder().append(time.getHour()).append(time.getMinute());
    }

    private static StringBuilder buildDoctorChapter(int doctorId) {
        final String doctorTicketPrefix = "D";

        return new StringBuilder(doctorTicketPrefix).append(doctorId);
    }

    public static String buildTicketToDoctor(int doctorId, LocalDate date, LocalTime time) {
        return buildDoctorChapter(doctorId)
                .append(convertDateToString(date))
                .append(convertTimeToString(time))
                .toString();
    }

    public static String buildTicketToCommission(LocalDate date, LocalTime time, List<Integer> doctorIds) {
        final String commissionTicketPrefix = "C";
        final StringBuilder result = new StringBuilder(commissionTicketPrefix);

        doctorIds.forEach(i -> result.append(buildDoctorChapter(i)));

        result.append(convertDateToString(date));
        result.append(convertTimeToString(time));

        return result.toString();
    }
}