package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Duration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DurationConstraintValidator implements ConstraintValidator<Duration, Integer> {
    @Override
    public boolean isValid(Integer durationField, ConstraintValidatorContext context) {
        return durationField > 0;
    }
}