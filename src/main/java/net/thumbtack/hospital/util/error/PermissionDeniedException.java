package net.thumbtack.hospital.util.error;

public class PermissionDeniedException extends Exception {
    private final PermissionDeniedErrorCode errorCode;

    public PermissionDeniedException(PermissionDeniedErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public PermissionDeniedErrorCode getErrorCode() {
        return errorCode;
    }
}