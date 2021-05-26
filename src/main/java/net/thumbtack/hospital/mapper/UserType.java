package net.thumbtack.hospital.mapper;

public enum UserType {
    USER("USER"),
    ADMINISTRATOR("ADMINISTRATOR"),
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR");

    private final String type;

    UserType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}