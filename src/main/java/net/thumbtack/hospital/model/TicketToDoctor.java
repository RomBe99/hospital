package net.thumbtack.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TicketToDoctor {
    private String number;
    private LocalDate date;
    private LocalTime time;
    private Doctor doctor;

    public TicketToDoctor() {
    }

    public TicketToDoctor(String number, LocalDate date, LocalTime time, Doctor doctor) {
        setNumber(number);
        setDate(date);
        setTime(time);
        setDoctor(doctor);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketToDoctor)) return false;
        TicketToDoctor that = (TicketToDoctor) o;
        return Objects.equals(getNumber(), that.getNumber()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getDoctor(), that.getDoctor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getDate(), getTime(), getDoctor());
    }

    @Override
    public String toString() {
        return "TicketToDoctor{" +
                "number='" + number + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", doctor=" + doctor +
                '}';
    }
}