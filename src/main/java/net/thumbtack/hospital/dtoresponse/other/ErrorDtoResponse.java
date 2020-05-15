package net.thumbtack.hospital.dtoresponse.other;

public class ErrorDtoResponse {
    private String errorCode;
    private String field;
    private String message;

    public ErrorDtoResponse() {
    }

    public ErrorDtoResponse(String errorCode, String field, String message) {
        setErrorCode(errorCode);
        setField(field);
        setMessage(message);
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}