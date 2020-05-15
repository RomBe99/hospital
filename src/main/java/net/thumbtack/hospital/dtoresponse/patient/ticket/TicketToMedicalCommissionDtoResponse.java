package net.thumbtack.hospital.dtoresponse.patient.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class TicketToMedicalCommissionDtoResponse extends TicketDtoResponse{
    private List<TicketToDoctorDtoResponse> tickets;

    public TicketToMedicalCommissionDtoResponse(String ticket, String room, LocalDate date, LocalTime time,
                                                List<TicketToDoctorDtoResponse> tickets) {
        super(ticket, room, date, time);

        setTickets(tickets);
    }

    public void setTickets(List<TicketToDoctorDtoResponse> tickets) {
        this.tickets = tickets;
    }

    public List<TicketToDoctorDtoResponse> getTickets() {
        return tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TicketToMedicalCommissionDtoResponse that = (TicketToMedicalCommissionDtoResponse) o;
        return Objects.equals(tickets, that.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tickets);
    }

    @Override
    public String toString() {
        return super.toString() + " TicketToMedicalCommission{" +
                "tickets=" + tickets +
                '}';
    }
}