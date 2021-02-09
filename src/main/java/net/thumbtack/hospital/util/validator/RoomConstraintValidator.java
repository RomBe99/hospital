package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Room;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoomConstraintValidator implements ConstraintValidator<Room, String> {
    @Override
    public boolean isValid(String roomField, ConstraintValidatorContext context) {
        return roomField != null && !roomField.isEmpty() && roomField.length() <= 30;
    }
}