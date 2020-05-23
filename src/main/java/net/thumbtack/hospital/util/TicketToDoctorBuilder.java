package net.thumbtack.hospital.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.StringJoiner;

public class TicketToDoctorBuilder {
    private static final String DATA_SEPARATOR = "-";

    public static String buildTicketTicketNumber(int doctorId, LocalDate date, LocalTime time) {
        return new StringJoiner(DATA_SEPARATOR)
                .add(String.valueOf(doctorId))
                .add(date.toString())
                .add(time.toString())
                .toString();
    }

    public static int getDoctorIdFromTicketNumber(String ticketNumber) {
        return Integer.parseInt(ticketNumber.split(DATA_SEPARATOR)[0]);
    }

    public static LocalDate getDateFromTicketNumber(String ticketNumber) {
        return LocalDate.parse(ticketNumber.split(DATA_SEPARATOR)[1]);
    }

    public static LocalTime getTimeFromTicketNumber(String ticketNumber) {
        return LocalTime.parse(ticketNumber.split(DATA_SEPARATOR)[2]);
    }
}