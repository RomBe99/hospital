package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.util.validator.annotation.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;

public class PhoneConstraintValidator implements ConstraintValidator<Phone, String> {
    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneField == null) {
            return false;
        }

        final String regex = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
        Map<String, Integer> numberPrefixToLength = new HashMap<>();
        numberPrefixToLength.put("8", 11);
        numberPrefixToLength.put("+7", 12);

        for (String prefix : numberPrefixToLength.keySet()) {
            if (phoneField.startsWith(prefix) && phoneField.length() >= numberPrefixToLength.get(prefix)) {
                return phoneField.matches(regex);
            }
        }

        return false;
    }
}