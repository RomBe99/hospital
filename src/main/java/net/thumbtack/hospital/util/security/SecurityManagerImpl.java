package net.thumbtack.hospital.util.security;

import net.thumbtack.hospital.dao.PermissionsDao;
import net.thumbtack.hospital.daoimpl.AdministratorDaoImpl;
import net.thumbtack.hospital.daoimpl.DoctorDaoImpl;
import net.thumbtack.hospital.daoimpl.PatientDaoImpl;
import net.thumbtack.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCode;
import net.thumbtack.hospital.util.error.PermissionDeniedException;

import java.util.Map;
import java.util.Set;

public class SecurityManagerImpl implements SecurityManager {
    private final Set<UserType> userPermissions;
    private final Map<UserType, PermissionsDao> userDaos = Map.of(
            UserType.PATIENT, new PatientDaoImpl(),
            UserType.ADMINISTRATOR, new AdministratorDaoImpl(),
            UserType.DOCTOR, new DoctorDaoImpl(),
            UserType.USER, new UserDaoImpl()
    );

    private SecurityManagerImpl(UserType... userPermissions) {
        this.userPermissions = Set.of(userPermissions);
    }

    public static SecurityManager getSecurityManager(UserType... userPermissions) {
        return new SecurityManagerImpl(userPermissions);
    }

    @Override
    public int hasPermission(String sessionId) throws PermissionDeniedException {
        if (userPermissions.isEmpty()) {
            return userDaos.get(UserType.USER).hasPermissions(sessionId);
        }

        for (var ut : userPermissions) {
            final var userId = userDaos.get(ut).hasPermissions(sessionId);

            if (userId != 0) {
                return userId;
            }
        }

        throw new PermissionDeniedException(PermissionDeniedErrorCode.PERMISSION_DENIED);
    }
}