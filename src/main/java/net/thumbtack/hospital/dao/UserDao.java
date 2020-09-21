package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.user.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface UserDao extends PermissionsDao {
    int login(String sessionId, String login, String password);

    void logout(String sessionId);

    Doctor getDoctorInformationWithoutSchedule(int doctorId);

    List<Doctor> getDoctorsBySpecialityWithoutSchedule(String specialty);

    Doctor getDoctorInformationWithSchedule(int patientId, int doctorId, LocalDate startDate, LocalDate endDate);

    List<Doctor> getDoctorsInformationWithSchedule(int patientId, String specialty, LocalDate startDate, LocalDate endDate);
}