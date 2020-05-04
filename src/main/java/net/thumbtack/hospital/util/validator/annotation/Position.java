package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.PositionConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositionConstraintValidator.class)
public @interface Position {
    String message() default "invalid_position";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}