package net.thumbtack.hospital.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface DoctorMapper {
    @Insert("INSERT INTO doctor VALUES (#{userId}, #{specialtyId}, #{cabinetId});")
    void insertDoctor(@Param("userId") int userId, @Param("specialtyId") int specialtyId, @Param("cabinetId") int cabinetId);

    @Delete("DELETE FROM doctor WHERE userId = #{id};")
    void removeDoctor(int id);

    @Delete("DELETE FROM doctor;")
    void deleteAll();
}