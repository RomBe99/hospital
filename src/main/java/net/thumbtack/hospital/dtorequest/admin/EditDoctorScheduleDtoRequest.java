package net.thumbtack.hospital.dtorequest.admin;

import net.thumbtack.hospital.dtorequest.schedule.DayScheduleDtoRequest;
import net.thumbtack.hospital.dtorequest.schedule.DtoRequestWithSchedule;
import net.thumbtack.hospital.dtorequest.schedule.WeekScheduleDtoRequest;

import java.util.List;

public class EditDoctorScheduleDtoRequest extends DtoRequestWithSchedule {
    public EditDoctorScheduleDtoRequest() {
    }

    public EditDoctorScheduleDtoRequest(String dateStart, String dateEnd, int duration, WeekScheduleDtoRequest weekSchedule) {
        super(dateStart, dateEnd, duration, weekSchedule);
    }

    public EditDoctorScheduleDtoRequest(String dateStart, String dateEnd, int duration, List<DayScheduleDtoRequest> weekDaysSchedule) {
        super(dateStart, dateEnd, duration, weekDaysSchedule);
    }
}