package net.thumbtack.hospital.mapper;

import org.apache.ibatis.annotations.Delete;

public interface DebugMapper {
    @Delete("DELETE FROM user WHERE login <> 'admin';")
    void clearUsers();

    @Delete("DELETE FROM administrator WHERE userId <> 1;")
    void clearAdministrators();

    @Delete("DELETE FROM doctor;")
    void clearDoctors();

    @Delete("DELETE FROM patient;")
    void clearPatients();

    @Delete("DELETE FROM logged_in_users;")
    void clearLoggedInUsers();

    @Delete("DELETE FROM schedule_cell;")
    void clearScheduleCells();

    @Delete("DELETE FROM time_cell;")
    void clearTimeCells();

    @Delete("DELETE FROM medical_commission;")
    void clearMedicalCommissions();

    @Delete("DELETE FROM commission_doctor;")
    void clearCommissionDoctors();
}