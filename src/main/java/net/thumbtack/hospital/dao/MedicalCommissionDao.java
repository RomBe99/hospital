package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;

import java.util.List;

public interface MedicalCommissionDao {
    void createMedicalCommission(TicketToMedicalCommission ticket);

    void denyMedicalCommission(String ticketTitle);

    List<TicketToMedicalCommission> getTicketsToMedicalCommission(int patientId);
}