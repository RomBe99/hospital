package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.dtorequest.patient.AppointmentToDoctorDtoRequest;
import net.thumbtack.hospital.util.validator.annotation.AppointmentToDoctor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AppointmentToDoctorValidator implements ConstraintValidator<AppointmentToDoctor, AppointmentToDoctorDtoRequest> {
    @Override
    public boolean isValid(AppointmentToDoctorDtoRequest request, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValidDoctorId = request.getDoctorId() > 0;
        boolean isValidDoctorSpeciality = request.getSpeciality() != null && !request.getSpeciality().isBlank();

        return isValidDoctorId ^ isValidDoctorSpeciality;
    }
}
