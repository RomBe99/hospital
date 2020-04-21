package net.thumbtack.hospital.mappers;

import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.User;

import java.util.List;

public interface UserMapper {
    int insertUser(User user);

    void updateUser(User user);

    void removeUser(int id);

    List<Doctor> getAllDoctors();

    Doctor getDoctorById(int id);

    void deleteAll();
}