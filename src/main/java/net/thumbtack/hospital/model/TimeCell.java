package net.thumbtack.hospital.model;

import java.time.LocalTime;
import java.util.Objects;

public class TimeCell {
    private LocalTime time;
    private Patient patient;
    private int duration;

    public TimeCell() {
    }

    public TimeCell(LocalTime time, Patient patient, int duration) {
        setTime(time);
        setPatient(patient);
        setDuration(duration);
    }

    public TimeCell(LocalTime time,  int duration) {
        this(time, null, duration);
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalTime getTime() {
        return time;
    }

    public Patient getPatient() {
        return patient;
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
                Objects.equals(getTime(), timeCell.getTime()) &&
                Objects.equals(getPatient(), timeCell.getPatient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTime(), getPatient(), getDuration());
    }

    @Override
    public String toString() {
        return "TimeCell{" +
                "time=" + time +
                ", patient=" + patient +
                ", duration=" + duration +
                '}';
    }
}