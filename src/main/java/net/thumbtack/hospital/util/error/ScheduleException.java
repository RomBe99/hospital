package net.thumbtack.hospital.util.error;

public class ScheduleException extends Exception {
    private final ScheduleErrorCode errorCode;

    public ScheduleException(ScheduleErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ScheduleErrorCode getErrorCode() {
        return errorCode;
    }
}