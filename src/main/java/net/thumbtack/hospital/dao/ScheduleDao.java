package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.schedule.ScheduleCell;
import net.thumbtack.hospital.model.ticket.TicketToDoctor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleDao {
    void insertSchedule(int doctorId, List<ScheduleCell> schedule);

    void editSchedule(int doctorId, LocalDate dateStart, LocalDate dateEnd, List<ScheduleCell> schedule);

    void appointmentToDoctor(int patientId, int doctorId, LocalDate date, LocalTime time);

    void denyTicket(String ticket);

    List<TicketToDoctor> getTicketsToDoctor(int patientId);
}