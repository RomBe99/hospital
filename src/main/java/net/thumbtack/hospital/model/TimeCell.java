package net.thumbtack.hospital.model;

import java.time.LocalTime;
import java.util.Objects;

// TODO Добавить валидацию через аннотации
public class TimeCell {
    private LocalTime time;
    private int duration;

    public TimeCell() {
    }

    public TimeCell(LocalTime time, int duration) {
        setTime(time);
        setDuration(duration);
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeCell)) return false;
        TimeCell timeCell = (TimeCell) o;
        return getDuration() == timeCell.getDuration() &&
                Objects.equals(getTime(), timeCell.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTime(), getDuration());
    }

    @Override
    public String toString() {
        return "TimeCell{" +
                "time=" + time +
                ", duration=" + duration +
                '}';
    }
}