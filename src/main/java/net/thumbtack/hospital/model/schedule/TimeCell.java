package net.thumbtack.hospital.model.schedule;

import net.thumbtack.hospital.model.user.Patient;

import java.time.LocalTime;
import java.util.Objects;

public class TimeCell {
    private LocalTime time;
    private Patient patient;
    private int duration;
    private String ticket;

    public TimeCell() {
    }

    public TimeCell(LocalTime time, Patient patient, int duration, String ticket) {
        setTime(time);
        setPatient(patient);
        setDuration(duration);
        setTicket(ticket);
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

    public void setTicket(String ticket) {
        this.ticket = ticket;
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

    public String getTicket() {
        return ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeCell)) return false;
        TimeCell timeCell = (TimeCell) o;
        return getDuration() == timeCell.getDuration() &&
                Objects.equals(getTime(), timeCell.getTime()) &&
                Objects.equals(getPatient(), timeCell.getPatient()) &&
                Objects.equals(getTicket(), timeCell.getTicket());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTime(), getPatient(), getDuration(), getTicket());
    }

    @Override
    public String toString() {
        return "TimeCell{" +
                "time=" + time +
                ", patient=" + patient +
                ", duration=" + duration +
                ", ticketName='" + ticket + '\'' +
                '}';
    }
}