package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.WeekDay;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WeekDayConstraintValidator implements ConstraintValidator<WeekDay, String> {
    @Override
    public boolean isValid(String weekDay, ConstraintValidatorContext context) {
        try {
            net.thumbtack.hospital.util.WeekDay.of(weekDay);
        } catch (IllegalArgumentException | NullPointerException ex) {
            return false;
        }

        return true;
    }
}