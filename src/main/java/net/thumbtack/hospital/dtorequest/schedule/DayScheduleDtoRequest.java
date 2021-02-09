package net.thumbtack.hospital.dtorequest.schedule;

import net.thumbtack.hospital.util.validator.annotation.WeekDay;

import java.util.Objects;

public class DayScheduleDtoRequest extends ScheduleDtoRequest {
    @WeekDay
    private String weekDay;

    public DayScheduleDtoRequest() {
    }

    public DayScheduleDtoRequest(String timeStart, String timeEnd, String weekDay) {
        super(timeStart, timeEnd);

        setWeekDay(weekDay);
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getWeekDay() {
        return weekDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DayScheduleDtoRequest that = (DayScheduleDtoRequest) o;
        return Objects.equals(weekDay, that.weekDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weekDay);
    }

    @Override
    public String toString() {
        return super.toString() + " DayScheduleDtoRequest{" +
                "weekDay='" + weekDay + '\'' +
                '}';
    }
}