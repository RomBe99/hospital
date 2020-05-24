package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.WeekDays;
import net.thumbtack.hospital.util.validator.annotation.WeekDayInt;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DayOfWeek;

public class WeekDayIntConstraintValidator implements ConstraintValidator<WeekDayInt, Integer> {
    @Override
    public boolean isValid(Integer weekDay, ConstraintValidatorContext constraintValidatorContext) {
        try {
            DayOfWeek.of(weekDay);
        } catch (IllegalArgumentException | NullPointerException ex) {
            return false;
        }

        return true;
    }
}