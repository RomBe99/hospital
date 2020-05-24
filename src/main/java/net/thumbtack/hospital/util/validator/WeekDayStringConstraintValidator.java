package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.WeekDays;
import net.thumbtack.hospital.util.validator.annotation.WeekDayString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WeekDayStringConstraintValidator implements ConstraintValidator<WeekDayString, String> {
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