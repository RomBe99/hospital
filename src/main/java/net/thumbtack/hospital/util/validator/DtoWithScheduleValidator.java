package net.thumbtack.hospital.util.validator;

import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.util.validator.annotation.DtoWithSchedule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Collection;

public class DtoWithScheduleValidator implements ConstraintValidator<DtoWithSchedule, DtoRequestWithSchedule> {
   public boolean isValid(DtoRequestWithSchedule request, ConstraintValidatorContext context) {
      final int maxScheduleCount = 1;
      int scheduleCount = 0;
      boolean isCollection;
      Object temp;
      Field[] fields = request.getClass().getSuperclass().getDeclaredFields();

      for (Field f : fields) {
         if (f.isAnnotationPresent(DtoWithSchedule.Schedule.class)) {
            if (scheduleCount > maxScheduleCount) {
               return false;
            }

            f.setAccessible(true);

            isCollection = f.getAnnotation(DtoWithSchedule.Schedule.class).isCollection();

            try {
               temp = f.get(request);

               if (isCollection && !((Collection<?>) temp).isEmpty()) {
                  scheduleCount++;
                  continue;
               }

               if (!isCollection && temp != null) {
                  scheduleCount++;
                  continue;
               }
            } catch (IllegalAccessException ignored) {
            }

            f.setAccessible(false);
         }
      }

      return true;
   }
}
