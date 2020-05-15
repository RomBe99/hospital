package net.thumbtack.hospital.dtoresponse.patient.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public abstract class TicketDtoResponse {
    private String ticket;
    private String room;
    private LocalDate date;
    private LocalTime time;

    public TicketDtoResponse(String ticket, String room, LocalDate date, LocalTime time) {
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
        if (o == null || getClass() != o.getClass()) return false;
        TicketDtoResponse that = (TicketDtoResponse) o;
        return Objects.equals(ticket, that.ticket) &&
                Objects.equals(room, that.room) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket, room, date, time);
    }

    @Override
    public String toString() {
        return "TicketDtoResponse{" +
                "ticket='" + ticket + '\'' +
                ", room='" + room + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}