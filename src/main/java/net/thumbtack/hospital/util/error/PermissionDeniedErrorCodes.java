package net.thumbtack.hospital.util.error;

public enum PermissionDeniedErrorCodes {
    PERMISSION_DENIED("PERMISSION_DENIED");

    private final String errorCode;

    PermissionDeniedErrorCodes(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}