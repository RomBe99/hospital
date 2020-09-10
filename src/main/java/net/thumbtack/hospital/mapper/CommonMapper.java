package net.thumbtack.hospital.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface CommonMapper extends Mapper {
    @Select("SELECT id FROM user_type WHERE name = #{userType};")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int getUserTypeId(String userType);

    @Select("SELECT id FROM doctor_specialty WHERE name = #{name};")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int getDoctorSpecialityIdByName(String name);

    @Select("SELECT id FROM cabinet WHERE name = #{name};")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int getCabinetIdByName(String name);

    @Select("SELECT name FROM user_type WHERE id = (SELECT userTypeId FROM user WHERE id = #{userId});")
    @Options(useGeneratedKeys = true, keyProperty = "name")
    String getUserTypeByUserId(int userId);

    @Select("SELECT EXISTS(SELECT * FROM time_cell WHERE title = #{ticketTitle} AND patientId IS NOT NULL);")
    boolean containsAppointment(String ticketTitle);
}