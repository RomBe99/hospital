package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.NameConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Name.List.class)
@Constraint(validatedBy = NameConstraintValidator.class)
public @interface Name {
    String message() default "INVALID_NAME";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean isPatronymic() default false;

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Name[] value();
    }
}