package net.thumbtack.hospital.util.error;

public enum PermissionDeniedErrorCode {
    PERMISSION_DENIED("PERMISSION_DENIED", "You do not have the required permissions.");

    private final String errorCode;
    private final String errorMessage;

    PermissionDeniedErrorCode(String errorCode, String errorMessage) {
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