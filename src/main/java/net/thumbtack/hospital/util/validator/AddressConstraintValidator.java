package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Address;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressConstraintValidator implements ConstraintValidator<Address, String> {
    @Override
    public boolean isValid(String addressField, ConstraintValidatorContext context) {
        return addressField != null && !addressField.isBlank() && addressField.length() <= 200;
    }
}