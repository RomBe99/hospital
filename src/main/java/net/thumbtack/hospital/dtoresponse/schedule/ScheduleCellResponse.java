package net.thumbtack.hospital.dtoresponse.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScheduleCellResponse {
    private String date;
    private List<ScheduleTimeCellResponse> daySchedule = new ArrayList<>();

    public ScheduleCellResponse() {
    }

    public ScheduleCellResponse(String date, List<ScheduleTimeCellResponse> daySchedule) {
        setDate(date);
        setDaySchedule(daySchedule);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDaySchedule(List<ScheduleTimeCellResponse> daySchedule) {
        this.daySchedule = daySchedule;
    }

    public String getDate() {
        return date;
    }

    public List<ScheduleTimeCellResponse> getDaySchedule() {
        return daySchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleCellResponse that = (ScheduleCellResponse) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(daySchedule, that.daySchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, daySchedule);
    }

    @Override
    public String toString() {
        return "ScheduleCellResponse{" +
                "date='" + date + '\'' +
                ", daySchedule=" + daySchedule +
                '}';
    }
}