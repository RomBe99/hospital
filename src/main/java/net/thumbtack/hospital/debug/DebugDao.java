package net.thumbtack.hospital.debug;

import net.thumbtack.hospital.model.schedule.ScheduleCell;

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

    List<ScheduleCell> getScheduleByDoctorId(int doctorId);
}