package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.user.Patient;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalTime;

public interface PatientMapper extends UserMapper {
    @Insert("INSERT INTO patient VALUES (#{id}, #{email}, #{address}, #{phone});")
    void insertPatient(Patient patient);

    @Update("UPDATE patient SET email = #{email}, address = #{address}, phone = #{phone} WHERE userId = #{id};")
    void updatePatient(Patient patient);

    @Select("SELECT userId FROM patient WHERE userId = (SELECT userId FROM logged_in_users WHERE sessionId = #{sessionId});")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int hasPermissions(String sessionId);

    @Update("UPDATE time_cell SET patientId = #{patientId} WHERE patientId IS NULL AND scheduleCellId = (SELECT id FROM schedule_cell WHERE doctorId = #{doctorId} AND date = #{date}) AND time = #{time};")
    void appointmentToDoctor(@Param("patientId") int patientId, @Param("doctorId") int doctorId,
                             @Param("date") LocalDate date, @Param("time") LocalTime time);

    @Delete("DELETE FROM medical_commission WHERE ticket = #{ticket};")
    void denyMedicalCommission(String ticket);

    @Update("UPDATE time_cell SET patientId = NULL WHERE ticket = #{ticket};")
    void denyTicket(String ticket);
}