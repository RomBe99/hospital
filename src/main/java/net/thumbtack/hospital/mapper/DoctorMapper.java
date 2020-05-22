package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.MedicalCommission;
import org.apache.ibatis.annotations.*;

public interface DoctorMapper extends UserMapper {
    @Insert("INSERT INTO doctor VALUES (#{userId}, #{specialtyId}, #{cabinetId});")
    void insertDoctor(@Param("userId") int userId, @Param("specialtyId") int specialtyId, @Param("cabinetId") int cabinetId);

    @Delete("DELETE FROM doctor WHERE userId = #{id};")
    void removeDoctor(int id);

    @Select("SELECT userId FROM doctor WHERE userId = (SELECT userId FROM logged_in_users WHERE sessionId = #{sessionId});")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int hasPermissions(String sessionId);

    @Insert("INSERT INTO medical_commission VALUES (#{id}, #{date}, #{time}, #{patientId}, #{duration});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createMedicalCommission(MedicalCommission medicalCommission);

    @Insert("INSERT INTO commission_doctor VALUES (#{commissionId}, #{doctorId});")
    void insertDoctorInMedicalCommission(@Param("commissionId") int commissionId, @Param("doctorId") int doctorId);
}