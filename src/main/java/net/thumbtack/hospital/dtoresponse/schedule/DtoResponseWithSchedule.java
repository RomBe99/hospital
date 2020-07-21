package net.thumbtack.hospital.dtoresponse.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DtoResponseWithSchedule {
    private List<ScheduleCellResponse> schedule = new ArrayList<>();

    public DtoResponseWithSchedule() {
    }

    public DtoResponseWithSchedule(List<ScheduleCellResponse> schedule) {
        setSchedule(schedule);
    }

    public void setSchedule(List<ScheduleCellResponse> schedule) {
        this.schedule = schedule == null ? new ArrayList<>() : schedule;
    }

    public List<ScheduleCellResponse> getSchedule() {
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