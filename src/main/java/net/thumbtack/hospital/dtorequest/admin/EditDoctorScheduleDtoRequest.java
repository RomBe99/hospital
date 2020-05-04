package net.thumbtack.hospital.dtorequest.admin;

import net.thumbtack.hospital.dtorequest.other.schedulecell.WeekDayScheduleCellDtoRequest;
import net.thumbtack.hospital.dtorequest.other.schedulecell.WeekScheduleCellDtoRequest;
import net.thumbtack.hospital.util.validator.annotation.Date;

import java.util.List;
import java.util.Objects;

public class EditDoctorScheduleDtoRequest {
    @Date
    private String dateStart;
    @Date
    private String dateEnd;
    private List<WeekScheduleCellDtoRequest> weekSchedule;
    private List<WeekDayScheduleCellDtoRequest> weekDaysSchedule;

    public EditDoctorScheduleDtoRequest() {
    }

    public EditDoctorScheduleDtoRequest(String dateStart, String dateEnd,
                                        List<WeekScheduleCellDtoRequest> weekSchedule,
                                        List<WeekDayScheduleCellDtoRequest> weekDaysSchedule) {
        setDateStart(dateStart);
        setDateEnd(dateEnd);
        setWeekSchedule(weekSchedule);
        setWeekDaysSchedule(weekDaysSchedule);
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setWeekSchedule(List<WeekScheduleCellDtoRequest> weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public void setWeekDaysSchedule(List<WeekDayScheduleCellDtoRequest> weekDaysSchedule) {
        this.weekDaysSchedule = weekDaysSchedule;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public List<WeekScheduleCellDtoRequest> getWeekSchedule() {
        return weekSchedule;
    }

    public List<WeekDayScheduleCellDtoRequest> getWeekDaysSchedule() {
        return weekDaysSchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditDoctorScheduleDtoRequest that = (EditDoctorScheduleDtoRequest) o;
        return Objects.equals(dateStart, that.dateStart) &&
                Objects.equals(dateEnd, that.dateEnd) &&
                Objects.equals(weekSchedule, that.weekSchedule) &&
                Objects.equals(weekDaysSchedule, that.weekDaysSchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateStart, dateEnd, weekSchedule, weekDaysSchedule);
    }

    @Override
    public String toString() {
        return "EditDoctorScheduleDtoRequest{" +
                "dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", weekSchedule=" + weekSchedule +
                ", weekDaysSchedule=" + weekDaysSchedule +
                '}';
    }
}