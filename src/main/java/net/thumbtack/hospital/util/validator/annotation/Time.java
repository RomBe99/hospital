package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.TimeConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeConstraintValidator.class)
public @interface Time {
    String message() default "invalid_time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}