package net.thumbtack.hospital.dao;

import java.time.LocalDate;

public interface CommonDao {
    String getUserTypeByUserId(int userId);

    boolean containsAppointment(int doctorId, LocalDate dateStart, LocalDate dateEnd);
}