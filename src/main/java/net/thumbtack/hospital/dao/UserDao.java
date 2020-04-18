package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.Doctor;

import java.util.List;

public interface UserDao {
    List<Doctor> getAllDoctors();

    Doctor getDoctorById(int id);
}