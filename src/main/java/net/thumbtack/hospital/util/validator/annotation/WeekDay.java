package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.WeekDayConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WeekDayConstraintValidator.class)
public @interface WeekDay {
    String message() default "invalid_week_day";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
