package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Speciality;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SpecialityConstraintValidator implements ConstraintValidator<Speciality, String> {
   @Override
   public boolean isValid(String specialityField, ConstraintValidatorContext context) {
      return specialityField != null && !specialityField.isEmpty() && specialityField.length() <= 30;
   }
}