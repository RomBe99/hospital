package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.WeekDayIntConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(WeekDayInt.List.class)
@Constraint(validatedBy = WeekDayIntConstraintValidator.class)
public @interface WeekDayInt {
    String message() default "INVALID_WEEK_DAY";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        WeekDayInt[] value();
    }
}