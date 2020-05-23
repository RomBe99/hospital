package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {
    @Insert("INSERT INTO user VALUES (#{user.id}, #{user.login}, #{user.password}, #{user.firstName}, #{user.lastName}, #{user.patronymic}, #{userTypeId});")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    int insertUser(@Param("user") User user, @Param("userTypeId") int userTypeId);

    @Update("UPDATE user SET password = #{password}, firstName = #{firstName}, lastName = #{lastName}, patronymic = #{patronymic} WHERE id = #{id};")
    void updateUser(User user);

    @Delete("DELETE FROM user WHERE id = #{id};")
    void removeUser(int id);

    @Insert("INSERT INTO logged_in_users VALUES(#{sessionId}, (SELECT id FROM user WHERE login = #{login} AND password = #{password}));")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int loginUser(@Param("sessionId") String sessionId, @Param("login") String login, @Param("password") String password);

    @Delete("DELETE FROM logged_in_users WHERE sessionId = #{sessionId};")
    void logoutUser(String sessionId);

    @Select("SELECT userId FROM logged_in_users WHERE sessionId = #{sessionId};")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int hasPermissions(String sessionId);
}