package net.thumbtack.hospital.dtorequest.other.schedulecell;

import net.thumbtack.hospital.util.validator.annotation.Date;
import net.thumbtack.hospital.util.validator.annotation.WeekDayString;

import java.util.Objects;

public class WeekDayScheduleCellDtoRequest {
    @WeekDayString
    private String weekDay;
    @Date
    private String timeStart;
    @Date
    private String timeEnd;

    public WeekDayScheduleCellDtoRequest() {
    }

    public WeekDayScheduleCellDtoRequest(String weekDay, String timeStart, String timeEnd) {
        setWeekDay(weekDay);
        setTimeStart(timeStart);
        setTimeEnd(timeEnd);
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getWeekDay() {
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
        return Objects.equals(weekDay, that.weekDay) &&
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
                "weekDay='" + weekDay + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                '}';
    }
}