package net.thumbtack.hospital.model.schedule;

import net.thumbtack.hospital.model.user.Patient;

import java.time.LocalTime;
import java.util.Objects;

public class TimeCell {
    private LocalTime time;
    private Patient patient;
    private int duration;
    private String title;

    public TimeCell() {
    }

    public TimeCell(LocalTime time, Patient patient, int duration, String title) {
        setTime(time);
        setPatient(patient);
        setDuration(duration);
        setTitle(title);
    }

    public TimeCell(LocalTime time, int duration, String title) {
        this(time, null, duration, title);
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

    public void setTitle(String title) {
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeCell)) return false;
        TimeCell timeCell = (TimeCell) o;
        return getDuration() == timeCell.getDuration() &&
                Objects.equals(getTime(), timeCell.getTime()) &&
                Objects.equals(getPatient(), timeCell.getPatient()) &&
                Objects.equals(getTitle(), timeCell.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTime(), getPatient(), getDuration(), getTitle());
    }

    @Override
    public String toString() {
        return "TimeCell{" +
                "time=" + time +
                ", patient=" + patient +
                ", duration=" + duration +
                ", ticketName='" + title + '\'' +
                '}';
    }
}