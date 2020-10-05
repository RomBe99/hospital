package net.thumbtack.hospital.dtoresponse.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ErrorsDtoResponse {
    List<ErrorDtoResponse> errors = new ArrayList<>();

    public ErrorsDtoResponse() {
    }

    public ErrorsDtoResponse(ErrorDtoResponse ... errors) {
        this(Arrays.asList(errors));
    }

    public ErrorsDtoResponse(List<ErrorDtoResponse> errors) {
        setErrors(errors);
    }

    public void setErrors(List<ErrorDtoResponse> errors) {
        this.errors = errors == null ? new ArrayList<>() : errors;
    }

    public List<ErrorDtoResponse> getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorsDtoResponse that = (ErrorsDtoResponse) o;
        return Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors);
    }

    @Override
    public String toString() {
        return "ErrorsDtoResponse{" +
                "errors=" + errors +
                '}';
    }
}