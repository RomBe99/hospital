package net.thumbtack.hospital.dtorequest.other.schedulecell;

import net.thumbtack.hospital.util.validator.annotation.Time;
import net.thumbtack.hospital.util.validator.annotation.WeekDay;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

public class WeekScheduleCellDtoRequest {
    @Time
    private String timeStart;
    @Time
    private String timeEnd;
    @NotEmpty
    private List<@WeekDay String> weekDays;

    public WeekScheduleCellDtoRequest() {
    }

    public WeekScheduleCellDtoRequest(String timeStart, String timeEnd, List<String> weekDays) {
        setTimeStart(timeStart);
        setTimeEnd(timeEnd);
        setWeekDays(weekDays);
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setWeekDays(List<String> weekDays) {
        this.weekDays = weekDays;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public List<String> getWeekDays() {
        return weekDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeekScheduleCellDtoRequest that = (WeekScheduleCellDtoRequest) o;
        return Objects.equals(timeStart, that.timeStart) &&
                Objects.equals(timeEnd, that.timeEnd) &&
                Objects.equals(weekDays, that.weekDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStart, timeEnd, weekDays);
    }

    @Override
    public String toString() {
        return "WeekScheduleCellDtoRequest{" +
                "timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", weekDays=" + weekDays +
                '}';
    }
}