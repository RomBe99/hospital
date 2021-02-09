package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.DayOfWeekConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DayOfWeek.List.class)
@Constraint(validatedBy = DayOfWeekConstraintValidator.class)
public @interface DayOfWeek {
    String message() default "INVALID_DAY_OF_WEEK";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        DayOfWeek[] value();
    }
}