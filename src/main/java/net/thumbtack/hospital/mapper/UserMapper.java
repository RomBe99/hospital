package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.user.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper extends Mapper {
    @Insert("INSERT INTO user VALUES (#{user.id}, #{user.login}, #{user.password}, #{user.firstName}, #{user.lastName}, #{user.patronymic}, #{userTypeId});")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    void insertUser(@Param("user") User user, @Param("userTypeId") int userTypeId);

    @Update("UPDATE user SET password = #{newPassword}, firstName = #{u.firstName}, lastName = #{u.lastName}, patronymic = #{u.patronymic} " +
            "WHERE id = #{u.id} AND password = #{u.password};")
    void updateUser(@Param("u") User user, @Param("newPassword") String newPassword);

    @Insert("INSERT INTO logged_in_users VALUES(#{sessionId}, #{userId});")
    void loginUser(@Param("sessionId") String sessionId, @Param("userId") int userId);

    @Delete("DELETE FROM logged_in_users WHERE sessionId = #{sessionId};")
    void logoutUser(String sessionId);

    @Select("SELECT userId FROM logged_in_users WHERE sessionId = #{sessionId};")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    Integer hasPermissions(String sessionId);

    @Select("SELECT id FROM user WHERE login = #{login} AND password = #{password};")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int getUserIdByLoginAndPassword(@Param("login") String login, @Param("password") String password);
}