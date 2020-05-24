package net.thumbtack.hospital.mapper;

public enum UserTypes {
    ADMINISTRATOR("ADMINISTRATOR"),
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR");

    private final String type;

    UserTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}