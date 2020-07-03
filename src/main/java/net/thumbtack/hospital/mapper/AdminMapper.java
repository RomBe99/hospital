package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.Administrator;
import net.thumbtack.hospital.model.ScheduleCell;
import net.thumbtack.hospital.model.TimeCell;
import org.apache.ibatis.annotations.*;

public interface AdminMapper extends UserMapper {
    @Insert("INSERT INTO administrator VALUES (#{id}, #{position});")
    void insertAdministrator(Administrator administrator);

    @Insert("INSERT INTO schedule_cell VALUES (#{cell.id}, #{doctorId}, #{cell.date});")
    @Options(useGeneratedKeys = true, keyProperty = "cell.id")
    void insertScheduleCell(@Param("doctorId") int doctorId, @Param("cell") ScheduleCell scheduleCell);

    @Insert("INSERT INTO time_cell VALUES (#{cell.time}, #{cell.ticket}, #{scheduleCellId}, NULL, #{cell.duration});")
    void insertTimeCell(@Param("scheduleCellId") int scheduleCellId, @Param("cell") TimeCell timeCell);

    @Update("UPDATE administrator SET position = #{position} WHERE userId = #{id};")
    void updateAdministrator(Administrator administrator);

    @Delete("DELETE FROM administrator WHERE userId = #{id};")
    void removeAdministratorById(int id);

    @Select("SELECT userId FROM administrator WHERE userId = (SELECT userId FROM logged_in_users WHERE sessionId = #{sessionId});")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int hasPermissions(String sessionId);
}