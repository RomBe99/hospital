package net.thumbtack.hospital.dao;

import net.thumbtack.hospital.util.error.PermissionDeniedException;

public interface PermissionsDao {
    int hasPermissions(String sessionId) throws PermissionDeniedException;
}