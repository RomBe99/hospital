package net.thumbtack.hospital.dtorequest.schedule;

import net.thumbtack.hospital.util.validator.annotation.Date;
import net.thumbtack.hospital.util.validator.annotation.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DtoRequestWithSchedule {
    @Date
    private String dateStart;
    @Date
    private String dateEnd;
    @Duration
    private int duration;
    private WeekScheduleDtoRequest weekSchedule;
    private List<DayScheduleDtoRequest> weekDaysSchedule = new ArrayList<>();

    public DtoRequestWithSchedule() {
    }

    private DtoRequestWithSchedule(String dateStart, String dateEnd, int duration,
                                   WeekScheduleDtoRequest weekSchedule, List<DayScheduleDtoRequest> weekDaysSchedule) {
        setDateStart(dateStart);
        setDateEnd(dateEnd);
        setDuration(duration);
        setWeekSchedule(weekSchedule);
        setWeekDaysSchedule(weekDaysSchedule);
    }

    public DtoRequestWithSchedule(String dateStart, String dateEnd, int duration, WeekScheduleDtoRequest weekSchedule) {
        this(dateStart, dateEnd, duration, weekSchedule, new ArrayList<>());
    }

    public DtoRequestWithSchedule(String dateStart, String dateEnd, int duration, List<DayScheduleDtoRequest> weekDaysSchedule) {
        this(dateStart, dateEnd, duration, null, weekDaysSchedule);
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setWeekSchedule(WeekScheduleDtoRequest weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public void setWeekDaysSchedule(List<DayScheduleDtoRequest> weekDaysSchedule) {
        this.weekDaysSchedule = weekDaysSchedule == null ? new ArrayList<>() : weekDaysSchedule;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public int getDuration() {
        return duration;
    }

    public WeekScheduleDtoRequest getWeekSchedule() {
        return weekSchedule;
    }

    public List<DayScheduleDtoRequest> getWeekDaysSchedule() {
        return weekDaysSchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DtoRequestWithSchedule that = (DtoRequestWithSchedule) o;
        return duration == that.duration &&
                Objects.equals(dateStart, that.dateStart) &&
                Objects.equals(dateEnd, that.dateEnd) &&
                Objects.equals(weekSchedule, that.weekSchedule) &&
                Objects.equals(weekDaysSchedule, that.weekDaysSchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateStart, dateEnd, duration, weekSchedule, weekDaysSchedule);
    }

    @Override
    public String toString() {
        return "DtoRequestWithSchedule{" +
                "dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", duration=" + duration +
                ", weekSchedule=" + weekSchedule +
                ", weekDaysSchedule=" + weekDaysSchedule +
                '}';
    }
}