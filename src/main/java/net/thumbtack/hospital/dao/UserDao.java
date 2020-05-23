package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.util.error.PermissionDeniedException;

public interface UserDao {
    int login(String sessionId, String login, String password);

    void logout(String sessionId);

    int hasPermissions(String sessionId) throws PermissionDeniedException;
}