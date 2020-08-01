package net.thumbtack.hospital.util.security.manager;

import net.thumbtack.hospital.util.error.PermissionDeniedException;

public interface SecurityManager {
    int hasPermission(String sessionId) throws PermissionDeniedException;
}