package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.LoginConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginConstraintValidator.class)
public @interface Login {
    String message() default "invalid_login";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}