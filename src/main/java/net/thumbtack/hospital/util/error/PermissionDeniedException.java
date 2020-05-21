package net.thumbtack.hospital.util.error;

public class PermissionDeniedException extends RuntimeException {
    private final PermissionDeniedErrorCodes errorCode;

    public PermissionDeniedException(PermissionDeniedErrorCodes errorCode) {
        this.errorCode = errorCode;
    }

    public PermissionDeniedErrorCodes getErrorCode() {
        return errorCode;
    }
}