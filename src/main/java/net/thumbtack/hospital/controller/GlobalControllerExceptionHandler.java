package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtoresponse.other.ErrorDtoResponse;
import net.thumbtack.hospital.dtoresponse.other.ErrorsDtoResponse;
import net.thumbtack.hospital.util.error.ErrorMessageFactory;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
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

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseBody
    public ErrorsDtoResponse handlePermissionExceptions(PermissionDeniedException ex) {
        String errorCode = ex.getErrorCode().getErrorCode();
        String field = "sessionId";
        String errorMessage = "You do not have the required permissions.";

        return new ErrorsDtoResponse(Collections.singletonList(new ErrorDtoResponse(errorCode, field, errorMessage)));
    }
}