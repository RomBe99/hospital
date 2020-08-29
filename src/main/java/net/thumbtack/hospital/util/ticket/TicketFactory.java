package net.thumbtack.hospital.util.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TicketFactory {
    private static final String DOCTOR_TICKET_PREFIX = "D";
    private static final String COMMISSION_TICKET_PREFIX = "C";

    private static String convertDateToString(LocalDate date) {
        return String.valueOf(date.getDayOfMonth()) + date.getMonthValue() + date.getYear();
    }

    private static String convertTimeToString(LocalTime time) {
        return String.valueOf(time.getHour()) + time.getMinute();
    }

    private static String buildDoctorChapter(int doctorId) {
        return DOCTOR_TICKET_PREFIX + doctorId;
    }

    public static String buildTicketToDoctor(int doctorId, LocalDate date, LocalTime time) {
        return buildDoctorChapter(doctorId) + convertDateToString(date) + convertTimeToString(time);
    }

    public static String buildTicketToCommission(LocalDate date, LocalTime time, List<Integer> doctorIds) {
        StringBuilder sb = new StringBuilder();

        for (Integer i : doctorIds) {
            sb.append(buildDoctorChapter(i));
        }

        return COMMISSION_TICKET_PREFIX + sb + convertDateToString(date) + convertTimeToString(time);
    }
}