package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.util.validator.annotation.DtoWithSchedule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DtoWithScheduleValidator implements ConstraintValidator<DtoWithSchedule, DtoRequestWithSchedule> {
    private final Map<Boolean, Predicate<Object>> fieldValidators = new HashMap<>();
    final int maxScheduleCount = 1;

    public DtoWithScheduleValidator() {
        fieldValidators.put(false, Objects::nonNull);
        fieldValidators.put(true, o -> !((Collection<?>) o).isEmpty());
    }

    public boolean isValid(DtoRequestWithSchedule request, ConstraintValidatorContext context) {
        List<Field> fieldsWithSchedule = Arrays.stream(request.getClass().getSuperclass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(DtoWithSchedule.Schedule.class))
                .collect(Collectors.toList());

        var scheduleCount = 0;
        boolean isCollection;
        Object temp;

        for (var f : fieldsWithSchedule) {
            f.setAccessible(true);

            isCollection = f.getAnnotation(DtoWithSchedule.Schedule.class).isCollection();

            try {
                temp = f.get(request);
                f.setAccessible(false);

                if (fieldValidators.get(isCollection).test(temp)) {
                    scheduleCount++;
                }
            } catch (IllegalAccessException | NullPointerException ex) {
                return false;
            }

            if (scheduleCount > maxScheduleCount) {
                return false;
            }
        }

        return true;
    }
}
