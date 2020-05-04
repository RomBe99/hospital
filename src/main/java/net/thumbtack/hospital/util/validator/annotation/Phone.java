package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.PhoneConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneConstraintValidator.class)
public @interface Phone {
    String message() default "invalid_phone";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}