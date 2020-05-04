package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.DurationConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationConstraintValidator.class)
public @interface Duration {
    String message() default "invalid_duration";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}