package net.thumbtack.hospital.util.error;

public enum DoctorNotFoundErrorCode {
    DOCTOR_NOT_FOUND("DOCTOR_NOT_FOUND", "Doctor by id or speciality not found.");

    private final String errorCode;
    private final String errorMessage;

    DoctorNotFoundErrorCode(String errorCode, String errorMessage) {
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