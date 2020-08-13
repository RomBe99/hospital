package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.user.Doctor;

import java.util.List;

public interface DoctorDao extends UserDao {
    void insertDoctor(Doctor doctor);

    void removeDoctor(int id);

    Doctor getDoctorById(int id);
}