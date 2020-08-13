package net.thumbtack.hospital.model.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public abstract class Ticket {
    private String title;
    private String room;
    private LocalDate date;
    private LocalTime time;

    public Ticket() {
    }

    public Ticket(String title, String room, LocalDate date, LocalTime time) {
        setTitle(title);
        setRoom(room);
        setDate(date);
        setTime(time);
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTitle() {
        return title;
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
        return Objects.equals(getTitle(), ticket1.getTitle()) &&
                Objects.equals(getRoom(), ticket1.getRoom()) &&
                Objects.equals(getDate(), ticket1.getDate()) &&
                Objects.equals(getTime(), ticket1.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getRoom(), getDate(), getTime());
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticket='" + title + '\'' +
                ", room='" + room + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}