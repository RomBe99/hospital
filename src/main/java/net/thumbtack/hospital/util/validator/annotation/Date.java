package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.DateConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateConstraintValidator.class)
public @interface Date {
    String message() default "invalid_date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}