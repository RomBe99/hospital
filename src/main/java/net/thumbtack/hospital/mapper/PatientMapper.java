package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.Patient;
import org.apache.ibatis.annotations.*;

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
}