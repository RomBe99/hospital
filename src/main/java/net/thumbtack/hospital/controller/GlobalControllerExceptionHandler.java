package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtoresponse.other.ErrorDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ErrorsDtoResponse;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private final ErrorMessageFactory errorMessageFactory;
    private final Map<ScheduleErrorCode, String> scheduleErrorCodeToFields = Map.of(
            ScheduleErrorCode.SCHEDULE_HAVE_APPOINTMENT, "schedule",
            ScheduleErrorCode.ALREADY_CONTAINS_APPOINTMENT, "date and time"
    );

    @Autowired
    public GlobalControllerExceptionHandler(ErrorMessageFactory errorMessageFactory) {
        this.errorMessageFactory = errorMessageFactory;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorsDtoResponse handleFieldValidationExceptions(MethodArgumentNotValidException ex) {
        return new ErrorsDtoResponse(
                ex.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> new ErrorDtoResponse(
                                fieldError.getDefaultMessage(),
                                fieldError.getField(),
                                errorMessageFactory.getValidationMessageByCode(fieldError.getDefaultMessage())
                        ))
                        .collect(Collectors.toList())
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ScheduleException.class)
    @ResponseBody
    public ErrorsDtoResponse handleScheduleExceptions(ScheduleException ex) {
        final var errorCode = ex.getErrorCode().getErrorCode();
        final var field = scheduleErrorCodeToFields.get(ex.getErrorCode());
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