package net.thumbtack.hospital.util;

public enum WeekDays {
    MONDAY("Mon"),
    TUESDAY("Tue"),
    WEDNESDAY("Wed"),
    THURSDAY("Thu"),
    FRIDAY("Fri");

    private final String name;

    WeekDays(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}