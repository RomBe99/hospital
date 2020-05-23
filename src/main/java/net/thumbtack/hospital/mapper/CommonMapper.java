package net.thumbtack.hospital.mapper;

import org.apache.ibatis.annotations.Select;

public interface CommonMapper {
    @Select("SELECT id FROM user_type WHERE name = #{userType};")
    int getUserTypeId(String userType);

    @Select("SELECT id FROM doctor_specialty WHERE name = #{name};")
    int getDoctorSpecialityIdByName(String name);

    @Select("SELECT id FROM cabinet WHERE name = #{name};")
    int getCabinetIdByName(String name);
}