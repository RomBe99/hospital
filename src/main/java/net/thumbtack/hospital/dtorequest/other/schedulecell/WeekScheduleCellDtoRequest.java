package net.thumbtack.hospital.dtorequest.other.schedulecell;

import java.util.List;
import java.util.Objects;

public class WeekScheduleCellDtoRequest {
    private String timeStart;
    private String timeEnd;
    private List<Integer> weekDays;

    public WeekScheduleCellDtoRequest() {
    }

    public WeekScheduleCellDtoRequest(String timeStart, String timeEnd, List<Integer> weekDays) {
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

    public void setWeekDays(List<Integer> weekDays) {
        this.weekDays = weekDays;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public List<Integer> getWeekDays() {
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
        return "WeekScheduleCell{" +
                "timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", weekDays=" + weekDays +
                '}';
    }
}