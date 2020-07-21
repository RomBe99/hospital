package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.DayOfWeek;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DayOfWeekConstraintValidator implements ConstraintValidator<DayOfWeek, Integer> {
   public boolean isValid(Integer dayNumber, ConstraintValidatorContext context) {
      try {
         java.time.DayOfWeek.of(dayNumber);
      } catch (IllegalArgumentException | NullPointerException ex) {
         return false;
      }

      return false;
   }
}
