package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.PhoneConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Phone.List.class)
@Constraint(validatedBy = PhoneConstraintValidator.class)
public @interface Phone {
    String message() default "INVALID_PHONE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        Phone[] value();
    }
}