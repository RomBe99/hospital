package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.user.Doctor;

public interface DoctorDao extends PermissionsDao {
    void insertDoctor(Doctor doctor);

    void removeDoctor(int id);

    Doctor getDoctorById(int id);
}