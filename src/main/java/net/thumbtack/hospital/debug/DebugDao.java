package net.thumbtack.hospital.debug;

import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;

import java.util.List;

public interface DebugDao {
    void clearUsers();

    void clearAdministrators();

    void clearDoctors();

    void clearPatients();

    void clearLoggedInUsers();

    void clearScheduleCells();

    void clearTimeCells();

    void clearMedicalCommissions();

    void clearCommissionDoctors();

    boolean containsPatientInTimeCell(int patientId, String ticketTitle);

    List<ScheduleCell> getScheduleByDoctorId(int doctorId);

    TicketToMedicalCommission getTicketToMedicalCommissionByTitle(String title);
}