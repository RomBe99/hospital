package net.thumbtack.hospital.mappers;

import net.thumbtack.hospital.model.Administrator;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.ScheduleCell;

import java.util.List;

public interface AdminMapper {
    Administrator insertAdministrator(Administrator administrator);

    Doctor insertDoctor(Doctor doctor);

    void updateAdministrator(Administrator administrator);

    void updateDoctorSchedule(Doctor doctor, List<ScheduleCell> newSchedule);

    void deleteDoctor(Doctor doctor);

    void deleteAll();
}