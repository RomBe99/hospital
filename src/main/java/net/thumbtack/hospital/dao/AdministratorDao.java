package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.model.user.Administrator;

public interface AdministratorDao extends PermissionsDao {
    void insertAdministrator(Administrator administrator);

    void updateAdministrator(Administrator administrator, String newPassword);

    Administrator getAdministratorById(int id);

    void removeAdministrator(int id);
}