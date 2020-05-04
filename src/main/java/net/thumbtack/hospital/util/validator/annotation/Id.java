package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.IdConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdConstraintValidator.class)
public @interface Id {
    String message() default "invalid_id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}