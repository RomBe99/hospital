package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.Doctor;

import java.util.List;

public interface DoctorDao {
    Doctor insertDoctor(Doctor doctor);

    void removeDoctor(int id);

    List<Doctor> getAllDoctors();

    Doctor getDoctorById(int id);
}