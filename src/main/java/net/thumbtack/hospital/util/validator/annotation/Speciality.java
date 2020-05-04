package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.SpecialityConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SpecialityConstraintValidator.class)
public @interface Speciality {
    String message() default "invalid_speciality";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}