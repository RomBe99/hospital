package net.thumbtack.hospital.dtoresponse.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScheduleCellDtoResponse {
    private LocalDate date;
    private List<ScheduleTimeCellDtoResponse> daySchedule = new ArrayList<>();

    public ScheduleCellDtoResponse() {
    }

    public ScheduleCellDtoResponse(LocalDate date, List<ScheduleTimeCellDtoResponse> daySchedule) {
        setDate(date);
        setDaySchedule(daySchedule);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDaySchedule(List<ScheduleTimeCellDtoResponse> daySchedule) {
        this.daySchedule = daySchedule;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<ScheduleTimeCellDtoResponse> getDaySchedule() {
        return daySchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleCellDtoResponse that = (ScheduleCellDtoResponse) o;
        return Objects.equals(date, that.date) && Objects.equals(daySchedule, that.daySchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, daySchedule);
    }

    @Override
    public String toString() {
        return "ScheduleCellResponse{" +
                "date=" + date +
                ", daySchedule=" + daySchedule +
                '}';
    }
}