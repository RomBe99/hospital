package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.user.Administrator;

public interface AdminDao extends PermissionsDao {
    void insertAdministrator(Administrator administrator);

    void updateAdministrator(Administrator administrator);

    Administrator getAdministratorById(int id);

    void removeAdministrator(int id);
}