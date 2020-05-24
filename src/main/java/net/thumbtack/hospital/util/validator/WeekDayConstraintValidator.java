package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.WeekDays;
import net.thumbtack.hospital.util.validator.annotation.WeekDay;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DayOfWeek;

public class WeekDayConstraintValidator implements ConstraintValidator<WeekDay, String> {
   @Override
   public boolean isValid(String weekDay, ConstraintValidatorContext context) {
      try {
         WeekDays.valueOf(weekDay);
      } catch (IllegalArgumentException | NullPointerException ex) {
         return false;
      }

      return true;
   }
}