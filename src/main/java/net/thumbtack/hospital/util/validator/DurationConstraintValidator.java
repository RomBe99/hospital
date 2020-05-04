package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Duration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DurationConstraintValidator implements ConstraintValidator<Duration, String> {
   @Override
   public boolean isValid(String durationField, ConstraintValidatorContext context) {
      int duration;

      try {
         duration = Integer.parseInt(durationField);
      } catch (NumberFormatException ex) {
         return false;
      }

      return duration > 0;
   }
}