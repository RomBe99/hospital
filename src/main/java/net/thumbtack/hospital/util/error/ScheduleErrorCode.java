package net.thumbtack.hospital.util.error;

public enum ScheduleErrorCode {
    PERIOD_HAVE_APPOINTMENT("PERIOD_HAVE_APPOINTMENT",
            "It is impossible to change the schedule, since there is an appointment with a doctor in this interval");

    private final String errorCode;
    private final String errorMessage;

    ScheduleErrorCode(String errorCode, String errorMessage) {
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