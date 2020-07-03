package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import org.apache.ibatis.annotations.*;

public interface DoctorMapper extends UserMapper {
    @Insert("INSERT INTO doctor VALUES (#{userId}, #{specialtyId}, #{cabinetId});")
    void insertDoctor(@Param("userId") int userId, @Param("specialtyId") int specialtyId, @Param("cabinetId") int cabinetId);

    @Delete("DELETE FROM doctor WHERE userId = #{id};")
    void removeDoctor(int id);

    @Select("SELECT userId FROM doctor WHERE userId = (SELECT userId FROM logged_in_users WHERE sessionId = #{sessionId});")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int hasPermissions(String sessionId);

    @Insert("INSERT INTO medical_commission VALUES (#{ticket}, #{date}, #{time}, #{patientId}, #{duration}, (SELECT id FROM cabinet WHERE name = #{room}));")
    void createMedicalCommission(TicketToMedicalCommission ticketToMedicalCommission);

    @Insert("INSERT INTO commission_doctor VALUES (#{commissionTicket}, #{doctorId});")
    void insertDoctorInMedicalCommission(@Param("commissionTicket") String commissionTicket, @Param("doctorId") int doctorId);
}