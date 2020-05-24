package net.thumbtack.hospital.debug;

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
}