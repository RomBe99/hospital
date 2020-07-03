package net.thumbtack.hospital.model.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public abstract class Ticket {
    private String ticket;
    private String room;
    private LocalDate date;
    private LocalTime time;

    public Ticket() {
    }

    public Ticket(String ticket, String room, LocalDate date, LocalTime time) {
        setTicket(ticket);
        setRoom(room);
        setDate(date);
        setTime(time);
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getTicket() {
        return ticket;
    }

    public String getRoom() {
        return room;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket1 = (Ticket) o;
        return Objects.equals(getTicket(), ticket1.getTicket()) &&
                Objects.equals(getRoom(), ticket1.getRoom()) &&
                Objects.equals(getDate(), ticket1.getDate()) &&
                Objects.equals(getTime(), ticket1.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicket(), getRoom(), getDate(), getTime());
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticket='" + ticket + '\'' +
                ", room='" + room + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}