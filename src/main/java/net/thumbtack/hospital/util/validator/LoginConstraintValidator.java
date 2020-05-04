package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Login;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginConstraintValidator implements ConstraintValidator<Login, String> {
    @Override
    public boolean isValid(String loginField, ConstraintValidatorContext constraintValidatorContext) {
        // Set of letters and numbers (latin + cyrillic)
        String regex = "^[а-яА-ЯёЁa-zA-Z0-9]+$";

        return loginField != null && loginField.length() <= Integer.parseInt("constraints.maxNameLength") && loginField.matches(regex);
    }
}