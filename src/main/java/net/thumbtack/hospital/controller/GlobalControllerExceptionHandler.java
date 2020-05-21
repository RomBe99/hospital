package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtoresponse.other.ErrorDtoResponse;
import net.thumbtack.hospital.util.error.ErrorMessageFactory;
import net.thumbtack.hospital.util.error.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

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
    public ErrorDtoResponse handleFieldValidationExceptions(MethodArgumentNotValidException ex) {
        String errorCode = ex.getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage();
        String field = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getField();
        String errorMessage = errorMessageFactory.getValidationMessageByCode(errorCode);

        return new ErrorDtoResponse(errorCode, field, errorMessage);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseBody
    public ErrorDtoResponse handlePermissionExceptions(PermissionDeniedException ex) {
        String errorCode = ex.getErrorCode().getErrorCode();
        String field = "sessionId";
        String errorMessage = "You do not have the required permissions.";

        return new ErrorDtoResponse(errorCode, field, errorMessage);
    }
}