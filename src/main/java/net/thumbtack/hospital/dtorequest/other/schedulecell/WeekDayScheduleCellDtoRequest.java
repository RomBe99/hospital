package net.thumbtack.hospital.dtorequest.other.schedulecell;

import net.thumbtack.hospital.util.validator.annotation.Date;
import net.thumbtack.hospital.util.validator.annotation.WeekDay;

import java.util.Objects;

public class WeekDayScheduleCellDtoRequest {
    @WeekDay
    private int weekDay;
    @Date
    private String timeStart;
    @Date
    private String timeEnd;

    public WeekDayScheduleCellDtoRequest() {
    }

    public WeekDayScheduleCellDtoRequest(int weekDay, String timeStart, String timeEnd) {
        setWeekDay(weekDay);
        setTimeStart(timeStart);
        setTimeEnd(timeEnd);
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeekDayScheduleCellDtoRequest that = (WeekDayScheduleCellDtoRequest) o;
        return weekDay == that.weekDay &&
                Objects.equals(timeStart, that.timeStart) &&
                Objects.equals(timeEnd, that.timeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekDay, timeStart, timeEnd);
    }

    @Override
    public String toString() {
        return "WeekDayScheduleCellDtoRequest{" +
                "weekDay=" + weekDay +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                '}';
    }
}