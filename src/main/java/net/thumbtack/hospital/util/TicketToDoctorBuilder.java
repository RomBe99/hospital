package net.thumbtack.hospital.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.StringJoiner;

public class TicketToDoctorBuilder {
    private static final String DATA_SEPARATOR = "-";
    private static final String PREFIX = "D";

    public static String buildTicketTicketNumber(int doctorId, LocalDate date, LocalTime time) {
        return PREFIX + DATA_SEPARATOR + new StringJoiner(DATA_SEPARATOR)
                .add(String.valueOf(doctorId))
                .add(date.toString())
                .add(time.toString())
                .toString();
    }

    public static int getDoctorIdFromTicketNumber(String ticketNumber) {
        return Integer.parseInt(ticketNumber.split(DATA_SEPARATOR)[1].replace(PREFIX, ""));
    }

    public static LocalDate getDateFromTicketNumber(String ticketNumber) {
        return LocalDate.parse(ticketNumber.split(DATA_SEPARATOR)[2]);
    }

    public static LocalTime getTimeFromTicketNumber(String ticketNumber) {
        return LocalTime.parse(ticketNumber.split(DATA_SEPARATOR)[3]);
    }
}