package net.thumbtack.hospital.util;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum WeekDay {
    MONDAY("Mon"),
    TUESDAY("Tue"),
    WEDNESDAY("Wed"),
    THURSDAY("Thu"),
    FRIDAY("Fri");

    private static final Map<String, WeekDay> NAME_TO_WEEK_DAY = Arrays.stream(values())
            .collect(Collectors.toMap(WeekDay::getName, w -> w));
    private static final Map<WeekDay, DayOfWeek> WEEK_DAY_TO_DAY_OF_WEEK = new HashMap<>();

    private final String name;

    WeekDay(String name) {
        this.name = name;
    }

    public static WeekDay of(String weekDayName) {
        WeekDay result = NAME_TO_WEEK_DAY.get(weekDayName);

        if (result == null) {
            throw new IllegalArgumentException("Invalid value for WeekDay: " + weekDayName);
        }

        return result;
    }

    public static DayOfWeek transformToDayOfWeek(WeekDay weekDay) {
        if (WEEK_DAY_TO_DAY_OF_WEEK.isEmpty()) {
            WEEK_DAY_TO_DAY_OF_WEEK.put(MONDAY, DayOfWeek.MONDAY);
            WEEK_DAY_TO_DAY_OF_WEEK.put(TUESDAY, DayOfWeek.TUESDAY);
            WEEK_DAY_TO_DAY_OF_WEEK.put(WEDNESDAY, DayOfWeek.WEDNESDAY);
            WEEK_DAY_TO_DAY_OF_WEEK.put(THURSDAY, DayOfWeek.THURSDAY);
            WEEK_DAY_TO_DAY_OF_WEEK.put(FRIDAY, DayOfWeek.FRIDAY);
        }

        return WEEK_DAY_TO_DAY_OF_WEEK.get(weekDay);
    }

    public static DayOfWeek transformToDayOfWeek(String weekDayName) {
        return transformToDayOfWeek(of(weekDayName));
    }

    public String getName() {
        return name;
    }
}