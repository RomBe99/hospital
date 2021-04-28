package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.configuration.Constraints;
import net.thumbtack.hospital.util.validator.annotation.Name;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameConstraintValidator implements ConstraintValidator<Name, String> {
    private boolean isPatronymic;
    private final Constraints constraints;

    @Autowired
    public NameConstraintValidator(Constraints constraints) {
        this.constraints = constraints;
    }

    @Override
    public void initialize(Name constraintAnnotation) {
        isPatronymic = constraintAnnotation.isPatronymic();
    }

    @Override
    public boolean isValid(String nameField, ConstraintValidatorContext context) {
        if (isPatronymic && (nameField == null || nameField.isBlank())) {
            return true;
        }

        if (nameField == null) {
            return false;
        }

        final var regex = "^[а-яА-ЯёЁa-zA-Z0-9]+$";

        return nameField.length() <= constraints.getMaxNameLength() && nameField.matches(regex);
    }
}