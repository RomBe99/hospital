package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Time;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TimeConstraintValidator implements ConstraintValidator<Time, String> {
   @Override
   public boolean isValid(String time, ConstraintValidatorContext context) {
      if (time == null) {
         return false;
      }

      try {
         LocalTime.parse(time);
      } catch (DateTimeParseException ignored) {
      }

      try {
         java.sql.Time.valueOf(time);
      } catch (IllegalArgumentException ex) {
         return false;
      }

      return true;
   }
}