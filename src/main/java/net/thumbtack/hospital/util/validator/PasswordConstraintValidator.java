package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.configuration.Constraints;
import net.thumbtack.hospital.util.validator.annotation.Password;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {
    private boolean isNewPassword;
    private final Constraints constraints;

    @Autowired
    public PasswordConstraintValidator(Constraints constraints) {
        this.constraints = constraints;
    }

    @Override
    public void initialize(Password constraintAnnotation) {
        isNewPassword = constraintAnnotation.isNewPassword();
    }

    @Override
    public boolean isValid(String passwordField, ConstraintValidatorContext context) {
        if (isNewPassword && (passwordField == null || passwordField.isBlank())) {
            return true;
        }

        if (passwordField == null) {
            return false;
        }

        if (passwordField.length() < constraints.getMinPasswordLength()
                || passwordField.length() > constraints.getMaxNameLength()) {
            return false;
        }

        // Latin lowercase and capital letters, numbers
        final var regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$";

        return passwordField.matches(regex);
    }
}