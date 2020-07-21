package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateConstraintValidator implements ConstraintValidator<Date, String> {
   @Override
   public boolean isValid(String dateField, ConstraintValidatorContext context) {
      if (dateField == null) {
         return false;
      }

      try {
         LocalDate.parse(dateField);
      } catch (DateTimeParseException ex) {
         return false;
      }

      return true;
   }
}