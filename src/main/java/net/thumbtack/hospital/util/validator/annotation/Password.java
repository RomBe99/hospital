package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface Password {
    String message() default "invalid_password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}