package net.thumbtack.hospital.mapper;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum UserType {
    USER("USER"),
    ADMINISTRATOR("ADMINISTRATOR"),
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR");

    private final String type;
    private static final Map<String, UserType> userTypes = Stream.of(values())
            .collect(Collectors.toMap(UserType::getType, userType -> userType));

    UserType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static UserType of(String type) {
        return userTypes.getOrDefault(type, USER);
    }
}