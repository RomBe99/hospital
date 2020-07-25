package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Position;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PositionConstraintValidator implements ConstraintValidator<Position, String> {
    @Override
    public boolean isValid(String positionField, ConstraintValidatorContext context) {
        return positionField != null && !positionField.isEmpty() && positionField.length() <= 200;
    }
}