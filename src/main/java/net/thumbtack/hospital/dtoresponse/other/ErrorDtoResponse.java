package net.thumbtack.hospital.dtoresponse.other;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDtoResponse that = (ErrorDtoResponse) o;
        return Objects.equals(errorCode, that.errorCode) &&
                Objects.equals(field, that.field) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, field, message);
    }

    @Override
    public String toString() {
        return "ErrorDtoResponse{" +
                "errorCode='" + errorCode + '\'' +
                ", field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}