package net.thumbtack.hospital.util.error;

public enum ScheduleErrorCode {
    SCHEDULE_HAVE_APPOINTMENT("SCHEDULE_HAVE_APPOINTMENT",
            "It is impossible to change the schedule, since there is an appointment with a doctor in this interval"),
    ALREADY_CONTAINS_APPOINTMENT("ALREADY_CONTAINS_APPOINTMENT",
            "You or another patient have appointment to this date and time");

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