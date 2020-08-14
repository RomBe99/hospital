package net.thumbtack.hospital.debug;

import net.thumbtack.hospital.mapper.Mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface DebugMapper extends Mapper {
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

    @Select("SELECT EXISTS(SELECT * FROM time_cell WHERE title = #{ticketTitle} AND patientId = {patientId});")
    boolean containsPatientInTimeCell(@Param("patientId") int patientId, @Param("ticketTitle") String ticketTitle);
}