package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Id;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdConstraintValidator implements ConstraintValidator<Id, String> {
   @Override
   public boolean isValid(String idField, ConstraintValidatorContext context) {
      int id;

      try {
         id = Integer.parseInt(idField);
      } catch (NumberFormatException ex) {
         return false;
      }

      return id >= 0;
   }
}