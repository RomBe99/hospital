package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.schedule.ScheduleCell;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleDao {
    void insertSchedule(int doctorId, List<ScheduleCell> schedule);

    void editSchedule(int doctorId, LocalDate dateStart, LocalDate dateEnd, List<ScheduleCell> schedule);
}