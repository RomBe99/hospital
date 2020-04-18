package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.Administrator;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.ScheduleCell;

import java.util.List;

public interface AdminDao {
    Administrator insertAdministrator(Administrator administrator);

    Doctor insertDoctor(Doctor doctor);

    void updateAdministrator(Administrator administrator);

    void updateDoctorSchedule(Doctor doctor, List<ScheduleCell> newSchedule);

    void deleteDoctor(int id);
}