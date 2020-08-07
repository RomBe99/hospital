package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;

public interface MedicalCommissionDao {
    void createMedicalCommission(TicketToMedicalCommission ticket);
}