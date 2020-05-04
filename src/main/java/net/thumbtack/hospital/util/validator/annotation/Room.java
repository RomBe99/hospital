package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.RoomConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoomConstraintValidator.class)
public @interface Room {
    String message() default "invalid_room";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}