package net.thumbtack.hospital.dtorequest.schedule;

import net.thumbtack.hospital.util.validator.annotation.DayOfWeek;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeekScheduleDtoRequest extends ScheduleDtoRequest {
    private List<@DayOfWeek Integer> weekDays = new ArrayList<>();

    public WeekScheduleDtoRequest() {
    }

    public WeekScheduleDtoRequest(String timeStart, String timeEnd, List<Integer> weekDays) {
        super(timeStart, timeEnd);

        setWeekDays(weekDays);
    }

    public WeekScheduleDtoRequest(String timeStart, String timeEnd) {
        this(timeStart, timeEnd, new ArrayList<>());
    }

    public void setWeekDays(List<Integer> weekDays) {
        this.weekDays = weekDays == null ? new ArrayList<>() : weekDays;
    }

    public List<Integer> getWeekDays() {
        return weekDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WeekScheduleDtoRequest that = (WeekScheduleDtoRequest) o;
        return Objects.equals(weekDays, that.weekDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weekDays);
    }

    @Override
    public String toString() {
        return super.toString() + " WeekScheduleDtoRequest{" +
                "weekDays=" + weekDays +
                '}';
    }
}