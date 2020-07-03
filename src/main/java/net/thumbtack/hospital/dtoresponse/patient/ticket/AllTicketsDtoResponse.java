package net.thumbtack.hospital.dtoresponse.patient.ticket;

import java.util.List;
import java.util.Objects;

public class AllTicketsDtoResponse {
    private List<TicketDtoResponse> tickets;

    public AllTicketsDtoResponse() {
    }

    public AllTicketsDtoResponse(List<TicketDtoResponse> tickets) {
        setTickets(tickets);
    }

    public void setTickets(List<TicketDtoResponse> tickets) {
        this.tickets = tickets;
    }

    public List<TicketDtoResponse> getTickets() {
        return tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllTicketsDtoResponse that = (AllTicketsDtoResponse) o;
        return Objects.equals(tickets, that.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tickets);
    }

    @Override
    public String toString() {
        return "AllTicketsDtoResponse{" +
                "tickets=" + tickets +
                '}';
    }
}