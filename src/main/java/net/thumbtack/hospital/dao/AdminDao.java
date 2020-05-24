package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.Administrator;
import net.thumbtack.hospital.model.ScheduleCell;

import java.time.LocalDate;
import java.util.List;

public interface AdminDao extends UserDao {
    Administrator insertAdministrator(Administrator administrator);

    void updateAdministrator(Administrator administrator);

    Administrator getAdministratorById(int id);

    void removeAdministratorById(int id);

    void editDoctorSchedule(LocalDate dateStart, LocalDate dateEnd, int doctorId, List<ScheduleCell> schedule);
}