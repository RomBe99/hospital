package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.NameConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NameConstraintValidator.class)
public @interface Name {
    String message() default "invalid_name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean isPatronymic() default false;
}