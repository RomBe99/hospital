package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {
    @Insert("INSERT INTO user VALUES (#{id}, #{login}, #{password}, #{firstName}, #{lastName}, #{patronymic});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);

    @Update("UPDATE user SET password = #{password}, firstName = #{firstName}, lastName = #{lastName}, patronymic = #{patronymic} WHERE id = #{id};")
    void updateUser(User user);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void removeUser(int id);

    @Select("SELECT id FROM user WHERE login = #{login} AND password = #{password};")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int tryLoginUser(@Param("login") String login, @Param("password") String password);

    @Delete("DELETE FROM user;")
    void deleteAll();
}