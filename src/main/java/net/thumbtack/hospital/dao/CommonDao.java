package net.thumbtack.hospital.dao;

import java.time.LocalDate;

public interface CommonDao {
    int getDoctorSpecialityIdByName(String name);

    int getCabinetIdByName(String name);

    String getUserTypeByUserId(int userId);

    boolean containsAppointment(int doctorId, LocalDate dateStart, LocalDate dateEnd);
}