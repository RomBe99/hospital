package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.util.error.PermissionDeniedException;

import java.time.LocalDate;
import java.util.List;

public interface UserDao {
    int login(String sessionId, String login, String password);

    void logout(String sessionId);

    int hasPermissions(String sessionId) throws PermissionDeniedException;

    Doctor getDoctorInformation(int patientId, int doctorId, LocalDate startDate, LocalDate endDate);

    List<Doctor> getDoctorsInformation(int patientId, String speciality, String startDate, String endDate);
}