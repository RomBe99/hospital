package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Password.List.class)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface Password {
    String message() default "INVALID_PASSWORD";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean isNewPassword() default false;

    @Documented
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        Password[] value();
    }
}