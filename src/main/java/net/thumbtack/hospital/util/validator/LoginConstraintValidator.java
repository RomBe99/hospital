package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.configuration.Constraints;
import net.thumbtack.hospital.util.validator.annotation.Login;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginConstraintValidator implements ConstraintValidator<Login, String> {
    private final Constraints constraints;

    @Autowired
    public LoginConstraintValidator(Constraints constraints) {
        this.constraints = constraints;
    }

    @Override
    public boolean isValid(String loginField, ConstraintValidatorContext constraintValidatorContext) {
        // Set of letters and numbers (latin + cyrillic)
        String regex = "^[а-яА-ЯёЁa-zA-Z0-9]+$";

        return loginField != null && loginField.length() <= constraints.getMaxNameLength() && loginField.matches(regex);
    }
}