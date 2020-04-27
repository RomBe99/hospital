package net.thumbtack.hospital.mappers;

import net.thumbtack.hospital.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    @Insert("INSERT INTO user VALUES (#{id}, #{login}, #{password}, #{firstName}, #{lastName}, #{patronymic});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);

    @Update("UPDATE user SET password = #{password}, firstName = #{firstName}, lastName = #{lastName}, patronymic = #{patronymic} WHERE id = #{id};")
    void updateUser(User user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void removeUser(int id);

    @Delete("DELETE FROM user;")
    void deleteAll();
}