package net.thumbtack.hospital.util.validator.annotation;

import net.thumbtack.hospital.util.validator.AppointmentToDoctorValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AppointmentToDoctorValidator.class)
public @interface AppointmentToDoctor {
    String message() default "INVALID_APPOINTMENT_TO_DOCTOR_REQUEST";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}