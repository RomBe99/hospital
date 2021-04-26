package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtoresponse.other.ErrorDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ErrorsDtoResponse;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private final ErrorMessageFactory errorMessageFactory;

    @Autowired
    public GlobalControllerExceptionHandler(ErrorMessageFactory errorMessageFactory) {
        this.errorMessageFactory = errorMessageFactory;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorsDtoResponse handleFieldValidationExceptions(MethodArgumentNotValidException ex) {
        final var errors = ex.getBindingResult().getFieldErrors();
        final var result = new ArrayList<ErrorDtoResponse>(errors.size());

        String errorCode;
        String field;
        String errorMessage;

        for (FieldError e : errors) {
            errorCode = e.getDefaultMessage();
            field = e.getField();
            errorMessage = errorMessageFactory.getValidationMessageByCode(errorCode);

            result.add(new ErrorDtoResponse(errorCode, field, errorMessage));
        }

        return new ErrorsDtoResponse(result);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ScheduleException.class)
    @ResponseBody
    public ErrorsDtoResponse handleScheduleExceptions(ScheduleException ex) {
        final var errorCodeToField = new HashMap<ScheduleErrorCode, String>();
        errorCodeToField.put(ScheduleErrorCode.SCHEDULE_HAVE_APPOINTMENT, "schedule");
        errorCodeToField.put(ScheduleErrorCode.ALREADY_CONTAINS_APPOINTMENT, "date and time");

        final var errorCode = ex.getErrorCode().getErrorCode();
        final var field = errorCodeToField.get(ex.getErrorCode());
        final var errorMessage = ex.getErrorCode().getErrorMessage();

        return new ErrorsDtoResponse(Collections.singletonList(new ErrorDtoResponse(errorCode, field, errorMessage)));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DoctorNotFoundException.class)
    @ResponseBody
    public ErrorsDtoResponse handleDoctorNotFoundException(DoctorNotFoundException ex) {
        final var errorCode = ex.getErrorCode().getErrorCode();
        final var field = "id or speciality";
        final var errorMessage = ex.getErrorCode().getErrorMessage();

        return new ErrorsDtoResponse(Collections.singletonList(new ErrorDtoResponse(errorCode, field, errorMessage)));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseBody
    public ErrorsDtoResponse handlePermissionExceptions(PermissionDeniedException ex) {
        final var errorCode = ex.getErrorCode().getErrorCode();
        final var field = CookieFactory.JAVA_SESSION_ID;
        final var errorMessage = ex.getErrorCode().getErrorMessage();

        return new ErrorsDtoResponse(Collections.singletonList(new ErrorDtoResponse(errorCode, field, errorMessage)));
    }
}