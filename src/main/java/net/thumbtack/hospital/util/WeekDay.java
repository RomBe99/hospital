package net.thumbtack.hospital.util;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WeekDay {
    MONDAY("Mon"),
    TUESDAY("Tue"),
    WEDNESDAY("Wed"),
    THURSDAY("Thu"),
    FRIDAY("Fri");

    private static final Map<String, WeekDay> NAME_TO_WEEK_DAY = Stream.of(values())
            .collect(Collectors.toMap(WeekDay::getName, w -> w));
    private static final Map<WeekDay, DayOfWeek> WEEK_DAY_TO_DAY_OF_WEEK = Map.of(
            MONDAY, DayOfWeek.MONDAY,
            TUESDAY, DayOfWeek.TUESDAY,
            WEDNESDAY, DayOfWeek.WEDNESDAY,
            THURSDAY, DayOfWeek.THURSDAY,
            FRIDAY, DayOfWeek.FRIDAY
    );

    private final String name;

    WeekDay(String name) {
        this.name = name;
    }

    public static WeekDay of(String weekDayName) {
        return Optional
                .ofNullable(NAME_TO_WEEK_DAY.get(weekDayName))
                .orElseThrow(() -> new IllegalArgumentException("Invalid value for WeekDay: " + weekDayName));
    }

    public static DayOfWeek transformToDayOfWeek(WeekDay weekDay) {
        return WEEK_DAY_TO_DAY_OF_WEEK.get(weekDay);
    }

    public static DayOfWeek transformToDayOfWeek(String weekDayName) {
        return transformToDayOfWeek(of(weekDayName));
    }

    public String getName() {
        return name;
    }
}