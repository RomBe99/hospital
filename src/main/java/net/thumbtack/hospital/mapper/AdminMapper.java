package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.user.Administrator;
import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AdminMapper extends UserMapper {
    @Insert("INSERT INTO administrator VALUES (#{id}, #{position});")
    void insertAdministrator(Administrator administrator);

    @Insert({"<script>",
            "INSERT INTO schedule_cell VALUES",
            "<foreach item='item' collection='cells' separator=','>",
            "(#{item.id}, #{doctorId}, #{item.date})",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "cells.id")
    void insertScheduleCells(@Param("doctorId") int doctorId, @Param("cells") List<ScheduleCell> scheduleCells);

    @Insert({"<script>",
            "INSERT INTO time_cell VALUES",
            "<foreach item='item' collection='cells' separator=','>",
            "(#{item.time}, #{item.ticket}, #{scheduleCellId}, NULL, #{item.duration})",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true)
    void insertTimeCells(@Param("scheduleCellId") int scheduleCellId, @Param("cells") List<TimeCell> timeCells);

    @Update("UPDATE administrator SET position = #{position} WHERE userId = #{id};")
    void updateAdministrator(Administrator administrator);

    @Delete("DELETE FROM administrator WHERE userId = #{id};")
    void removeAdministratorById(int id);

    @Select("SELECT userId FROM administrator WHERE userId = (SELECT userId FROM logged_in_users WHERE sessionId = #{sessionId});")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int hasPermissions(String sessionId);
}