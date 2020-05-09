package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Name;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameConstraintValidator implements ConstraintValidator<Name, String> {
   private boolean isPatronymic;

   @Override
   public void initialize(Name constraintAnnotation) {
      isPatronymic = constraintAnnotation.isPatronymic();
   }

   @Override
   public boolean isValid(String nameField, ConstraintValidatorContext context) {
      if (nameField == null) {
         return false;
      }

      if (isPatronymic && nameField.isEmpty()) {
         return true;
      }

      String regex = "^[а-яА-ЯёЁa-zA-Z0-9]+$";

      return nameField.length() <= Integer.parseInt("constraints.maxNameLength") && nameField.matches(regex);
   }
}