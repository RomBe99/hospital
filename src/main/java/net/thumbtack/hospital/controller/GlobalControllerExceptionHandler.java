package net.thumbtack.hospital.controller;

import net.thumbtack.hospital.dtoresponse.other.ErrorDtoResponse;
import net.thumbtack.hospital.util.error.ErrorMessageFactory;
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
    public ErrorDtoResponse handleFieldValidation(MethodArgumentNotValidException ex) {
        String errorCode = ex.getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage();
        String field = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getField();
        String errorMessage = errorMessageFactory.getValidationMessageByCode(errorCode);

        return new ErrorDtoResponse(errorCode, field, errorMessage);
    }
}