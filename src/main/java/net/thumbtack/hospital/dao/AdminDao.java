package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.Administrator;

public interface AdminDao {
    Administrator insertAdministrator(Administrator administrator);

    void updateAdministrator(Administrator administrator);

    Administrator getAdministratorById(int id);

    void removeAdministratorById(int id);
}