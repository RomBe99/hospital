package net.thumbtack.hospital.dao;

public interface CommonDao {
    int getDoctorSpecialityIdByName(String name);

    int getCabinetIdByName(String name);

    void clear();
}