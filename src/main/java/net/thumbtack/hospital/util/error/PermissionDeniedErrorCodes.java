package net.thumbtack.hospital.util.error;

public enum PermissionDeniedErrorCodes {
    PERMISSION_DENIED("PERMISSION_DENIED", "You do not have the required permissions.");

    private final String errorCode;
    private final String errorMessage;

    PermissionDeniedErrorCodes(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}