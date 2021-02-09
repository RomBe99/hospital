package net.thumbtack.hospital.util.error;

public class DoctorNotFoundException extends Exception {
    private final DoctorNotFoundErrorCode errorCode;

    public DoctorNotFoundException(DoctorNotFoundErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public DoctorNotFoundErrorCode getErrorCode() {
        return errorCode;
    }
}