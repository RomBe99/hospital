package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtoresponse.other.ErrorDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ErrorsDtoResponse;
import net.thumbtack.hospital.util.cookie.CookieFactory;
import net.thumbtack.hospital.util.error.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        List<ErrorDtoResponse> result = new ArrayList<>(errors.size());

        String errorCode;
        String field;
        String errorMessage;

        for (ObjectError e : errors) {
            errorCode = e.getDefaultMessage();
            field = ((FieldError) e).getField();
            errorMessage = errorMessageFactory.getValidationMessageByCode(errorCode);

            result.add(new ErrorDtoResponse(errorCode, field, errorMessage));
        }

        return new ErrorsDtoResponse(result);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ScheduleException.class)
    @ResponseBody
    public ErrorsDtoResponse handleScheduleExceptions(ScheduleException ex) {
        Map<ScheduleErrorCode, String> errorCodeToField = new HashMap<>();
        errorCodeToField.put(ScheduleErrorCode.SCHEDULE_HAVE_APPOINTMENT, "schedule");
        errorCodeToField.put(ScheduleErrorCode.ALREADY_CONTAINS_APPOINTMENT, "date and time");

        String errorCode = ex.getErrorCode().getErrorCode();
        String field = errorCodeToField.get(ex.getErrorCode());
        String errorMessage = ex.getErrorCode().getErrorMessage();

        return new ErrorsDtoResponse(Collections.singletonList(new ErrorDtoResponse(errorCode, field, errorMessage)));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DoctorNotFoundException.class)
    @ResponseBody
    public ErrorsDtoResponse handleDoctorNotFoundException(DoctorNotFoundException ex) {
        final String errorCode = ex.getErrorCode().getErrorCode();
        final String field = "id or speciality";
        final String errorMessage = ex.getErrorCode().getErrorMessage();

        return new ErrorsDtoResponse(Collections.singletonList(new ErrorDtoResponse(errorCode, field, errorMessage)));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseBody
    public ErrorsDtoResponse handlePermissionExceptions(PermissionDeniedException ex) {
        String errorCode = ex.getErrorCode().getErrorCode();
        String field = CookieFactory.JAVA_SESSION_ID;
        String errorMessage = ex.getErrorCode().getErrorMessage();

        return new ErrorsDtoResponse(Collections.singletonList(new ErrorDtoResponse(errorCode, field, errorMessage)));
    }
}