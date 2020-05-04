package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.WeekDay;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DayOfWeek;

public class WeekDayConstraintValidator implements ConstraintValidator<WeekDay, Integer> {
   @Override
   public boolean isValid(Integer weekDay, ConstraintValidatorContext context) {
      try {
         DayOfWeek.of(weekDay);
      } catch (IllegalArgumentException | NullPointerException ex) {
         return false;
      }

      return true;
   }
}