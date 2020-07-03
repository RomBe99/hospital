package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneConstraintValidator implements ConstraintValidator<Phone, String> {
    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneField == null) {
            return false;
        }

        int phoneLength = 11;
        String regex = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";

        if (phoneField.startsWith("8") && phoneField.length() >= phoneLength) {
            return phoneField.matches(regex);
        }

        if (phoneField.startsWith("+7") && phoneField.length() >= phoneLength + 1) {
            return phoneField.matches(regex);
        }

        return false;
    }
}