package net.thumbtack.hospital.mappers;

import net.thumbtack.hospital.model.Administrator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

public interface AdminMapper {
    @Insert("INSERT INTO administrator VALUES (#{id}, #{position});")
    void insertAdministrator(Administrator administrator);

    @Update("UPDATE administrator SET position = #{position} WHERE userId = #{id};")
    void updateAdministrator(Administrator administrator);

    @Delete("DELETE FROM administrator WHERE userId = #{id};")
    void removeAdministratorById(int id);

    @Delete("DELETE FROM administrator;")
    void deleteAll();
}