package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.AddressConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AddressConstraintValidator.class)
public @interface Address {
    String message() default "invalid_address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}