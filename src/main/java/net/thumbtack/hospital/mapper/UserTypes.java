package net.thumbtack.hospital.mapper;

public enum UserTypes {
    ADMINISTRATOR("Administrator"),
    PATIENT("Doctor"),
    DOCTOR("Patient");

    private final String type;

    UserTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}