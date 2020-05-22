package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.Patient;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalTime;

public interface PatientMapper extends UserMapper {
    @Insert("INSERT INTO patient VALUES (#{id}, #{email}, #{address}, #{phone});")
    void insertPatient(Patient patient);

    @Update("UPDATE patient SET email = #{email}, address = #{address}, phone = #{phone} WHERE userId = #{id};")
    void updatePatient(Patient patient);

    @Delete("DELETE FROM patient WHERE userId = #{id};")
    void removePatient(int id);

    @Select("SELECT userId FROM patient WHERE userId = (SELECT userId FROM logged_in_users WHERE sessionId = #{sessionId});")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int hasPermissions(String sessionId);

    @Update("UPDATE time_cell SET patientId = #{patientId} WHERE patientId IS NOT NULL, scheduleCellId = (SELECT id FROM schedule_cell WHERE doctorId = #{doctorId} AND date = #{date}) AND ticketTime = #{time};")
    void appointmentToDoctor(@Param("patientId") int patientId, @Param("doctorId") int doctorId,
                             @Param("date") LocalDate date, @Param("time") LocalTime time);

    @Delete("DELETE FROM medical_commission WHERE patientId = #{patientId} AND id = #{ticketId};")
    void denyMedicalCommission(@Param("patientId") int patientId, @Param("ticketId") int commissionTicketId);

    @Update("UPDATE time_cell SET patientId = NULL WHERE scheduleCellId = #{patientId} AND patientId = #{scheduleCellId};")
    void denyTicket(@Param("patientId") int patientId, @Param("scheduleCellId") int scheduleCellId);
}