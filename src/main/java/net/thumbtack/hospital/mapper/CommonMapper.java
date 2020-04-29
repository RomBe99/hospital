package net.thumbtack.hospital.mapper;

import org.apache.ibatis.annotations.Select;

public interface CommonMapper {
    @Select("SELECT name FROM doctor_specialty WHERE id = #{id};")
    String getDoctorSpecialityById(int id);

    @Select("SELECT name FROM cabinet WHERE id = #{id};")
    String getCabinetById(int id);

    @Select("SELECT id FROM doctor_specialty WHERE name = #{name};")
    int getDoctorSpecialityIdByName(String name);

    @Select("SELECT id FROM cabinet WHERE name = #{name};")
    int getCabinetIdByName(String name);
}