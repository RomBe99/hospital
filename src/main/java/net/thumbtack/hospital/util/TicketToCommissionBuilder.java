package net.thumbtack.hospital.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.StringJoiner;

public class TicketToCommissionBuilder {
    private static final String PREFIX = "CD";
    private static final String SEPARATOR = "D";

    public static String buildTicketNumber(List<Integer> doctorIds, LocalDate ticketDate, LocalTime ticketTime) {
        StringJoiner sj = new StringJoiner(SEPARATOR);

        for (int id : doctorIds) {
            sj.add(String.valueOf(id));
        }

        return PREFIX + sj.toString() + ticketDate.toString() + ticketTime.toString();
    }
}