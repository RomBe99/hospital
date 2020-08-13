package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleMapper extends Mapper {
    @Insert({"<script>",
            "INSERT INTO schedule_cell VALUES",
            "<foreach item='cell' collection='cells' separator=','>",
            "(#{cell.id}, #{doctorId}, #{cell.date})",
            "</foreach>;",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "cells.id")
    void insertScheduleCells(@Param("doctorId") int doctorId, @Param("cells") List<ScheduleCell> scheduleCells);

    @Insert({"<script>",
            "INSERT INTO time_cell VALUES",
            "<foreach item='cell' collection='cells' separator=','>",
            "(#{cell.time}, #{cell.title}, #{scheduleCellId}, NULL, #{cell.duration})",
            "</foreach>;",
            "</script>"})
    @Options(useGeneratedKeys = true)
    void insertTimeCells(@Param("scheduleCellId") int scheduleCellId, @Param("cells") List<TimeCell> timeCells);

    @Delete("DELETE FROM schedule_cell WHERE doctorId = #{doctorId} AND date >= #{startDate} AND date <= #{endDate};")
    void removeSchedule(@Param("doctorId") int doctorId,
                        @Param("startDate") LocalDate startDate, @Param("endStart") LocalDate endStart);

    @Update("UPDATE time_cell SET patientId = #{patientId} WHERE patientId IS NULL AND scheduleCellId = (SELECT id FROM schedule_cell WHERE doctorId = #{doctorId} AND date = #{date}) AND time = #{time};")
    void appointmentToDoctor(@Param("patientId") int patientId, @Param("doctorId") int doctorId,
                             @Param("date") LocalDate date, @Param("time") LocalTime time);

    @Update("UPDATE time_cell SET patientId = NULL WHERE ticket = #{ticket};")
    void denyTicket(String ticket);
}