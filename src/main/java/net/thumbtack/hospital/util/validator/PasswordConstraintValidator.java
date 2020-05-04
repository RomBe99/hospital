package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {
   @Override
   public boolean isValid(String passwordField, ConstraintValidatorContext context) {
      if (passwordField == null) {
         return false;
      }

      if (passwordField.length() < Integer.parseInt("constraints.minPasswordLength")
              || passwordField.length() > Integer.parseInt("constraints.maxNameLength")) {
         return false;
      }

      // Latin lowercase and capital letters, numbers
      String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$";

      return passwordField.matches(regex);
   }
}