package net.thumbtack.hospital.dtoresponse.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DtoResponseWithSchedule {
    private List<ScheduleCellDtoResponse> schedule = new ArrayList<>();

    public DtoResponseWithSchedule() {
    }

    public DtoResponseWithSchedule(List<ScheduleCellDtoResponse> schedule) {
        setSchedule(schedule);
    }

    public void setSchedule(List<ScheduleCellDtoResponse> schedule) {
        this.schedule = schedule == null ? new ArrayList<>() : schedule;
    }

    public List<ScheduleCellDtoResponse> getSchedule() {
        return schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DtoResponseWithSchedule that = (DtoResponseWithSchedule) o;
        return Objects.equals(schedule, that.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedule);
    }

    @Override
    public String toString() {
        return "DtoResponseWithSchedule{" +
                "schedule=" + schedule +
                '}';
    }
}