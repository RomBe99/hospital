package net.thumbtack.hospital.dao;

public interface PermissionsDao {
    int hasPermissions(String sessionId);
}