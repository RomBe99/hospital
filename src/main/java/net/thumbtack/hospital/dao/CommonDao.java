package net.thumbtack.hospital.dao;

import java.time.LocalDate;

public interface CommonDao {
    String getUserTypeByUserId(int userId);

    boolean containsAppointments(int doctorId, LocalDate dateStart, LocalDate dateEnd);

    boolean containsAppointment(String ticketTitle);
}