package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.schedule.TimeCell;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
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
            "(#{cell.time}, #{cell.ticket}, #{scheduleCellId}, NULL, #{cell.duration})",
            "</foreach>;",
            "</script>"})
    @Options(useGeneratedKeys = true)
    void insertTimeCells(@Param("scheduleCellId") int scheduleCellId, @Param("cells") List<TimeCell> timeCells);

    @Delete("DELETE FROM schedule_cell WHERE doctorId = #{doctorId} AND date >= #{startDate} AND date <= #{endDate};")
    void removeSchedule(@Param("doctorId") int doctorId,
                        @Param("startDate") LocalDate startDate, @Param("endStart") LocalDate endStart);
}