package net.thumbtack.hospital.dtorequest.schedule;

import net.thumbtack.hospital.util.validator.annotation.Time;

import java.util.Objects;

public abstract class ScheduleDtoRequest {
    @Time
    private String timeStart;
    @Time
    private String timeEnd;

    public ScheduleDtoRequest() {
    }

    public ScheduleDtoRequest(String timeStart, String timeEnd) {
        setTimeStart(timeStart);
        setTimeEnd(timeEnd);
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
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
        ScheduleDtoRequest that = (ScheduleDtoRequest) o;
        return Objects.equals(timeStart, that.timeStart) &&
                Objects.equals(timeEnd, that.timeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStart, timeEnd);
    }

    @Override
    public String toString() {
        return "WeekScheduleDtoRequest{" +
                "timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                '}';
    }
}