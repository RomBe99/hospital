package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.user.Administrator;
import org.apache.ibatis.annotations.*;

public interface AdministratorMapper extends UserMapper {
    @Insert("INSERT INTO administrator VALUES (#{id}, #{position});")
    void insertAdministrator(Administrator administrator);

    @Update("UPDATE administrator SET position = #{position} WHERE userId = #{id};")
    void updateAdministrator(Administrator administrator);

    @Delete("DELETE FROM administrator WHERE userId = #{id};")
    void removeAdministratorById(int id);

    @Select("SELECT userId FROM administrator WHERE userId = (SELECT userId FROM logged_in_users WHERE sessionId = #{sessionId});")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    Integer hasPermissions(String sessionId);
}