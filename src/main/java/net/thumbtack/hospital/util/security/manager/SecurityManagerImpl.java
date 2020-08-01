package net.thumbtack.hospital.util.security.manager;

import net.thumbtack.hospital.dao.UserDao;
import net.thumbtack.hospital.daoimpl.AdminDaoImpl;
import net.thumbtack.hospital.daoimpl.DoctorDaoImpl;
import net.thumbtack.hospital.daoimpl.PatientDaoImpl;
import net.thumbtack.hospital.daoimpl.UserDaoImpl;
import net.thumbtack.hospital.mapper.UserType;
import net.thumbtack.hospital.util.error.PermissionDeniedErrorCode;
import net.thumbtack.hospital.util.error.PermissionDeniedException;

import java.util.*;

public class SecurityManagerImpl implements SecurityManager{
    private final Set<UserType> userPermissions = new HashSet<>();
    private final Map<UserType, UserDao> userDaos = new HashMap<>();

    private SecurityManagerImpl(UserType... userPermissions) {
        this.userPermissions.addAll(Arrays.asList(userPermissions));

        userDaos.put(UserType.PATIENT, new PatientDaoImpl());
        userDaos.put(UserType.ADMINISTRATOR, new AdminDaoImpl());
        userDaos.put(UserType.DOCTOR, new DoctorDaoImpl());
        userDaos.put(null, new UserDaoImpl());
    }

    public static SecurityManager getSecurityManager(UserType... userPermissions) {
        return new SecurityManagerImpl(userPermissions);
    }

    @Override
    public int hasPermission(String sessionId) throws PermissionDeniedException {
        if (userPermissions.isEmpty()) {
            return userDaos.get(null).hasPermissions(sessionId);
        }

        for (UserType ut : userPermissions) {
            try {
                return userDaos.get(ut).hasPermissions(sessionId);
            } catch (PermissionDeniedException ignored) {
            }
        }

        throw new PermissionDeniedException(PermissionDeniedErrorCode.PERMISSION_DENIED);
    }
}