package net.thumbtack.hospital.mapper;

import org.apache.ibatis.annotations.*;

public interface DoctorMapper extends UserMapper {
    @Insert("INSERT INTO doctor VALUES (#{userId}, #{specialtyId}, #{cabinetId});")
    void insertDoctor(@Param("userId") int userId, @Param("specialtyId") int specialtyId, @Param("cabinetId") int cabinetId);

    @Delete("DELETE FROM doctor WHERE userId = #{id};")
    void removeDoctor(int id);
}