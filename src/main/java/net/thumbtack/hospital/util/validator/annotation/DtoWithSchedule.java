package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.DtoWithScheduleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DtoWithScheduleValidator.class)
public @interface DtoWithSchedule {
    String message() default "INVALID_SCHEDULE_REQUEST";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Schedule {
        boolean isCollection() default false;
    }
}
