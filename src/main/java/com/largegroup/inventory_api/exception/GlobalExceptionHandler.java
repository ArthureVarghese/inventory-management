package com.largegroup.inventory_api.exception;

import com.largegroup.inventory_api.view.ErrorResponse;
import com.largegroup.inventory_api.view.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler (ValidationError.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationError(ValidationError ex){
        return new ErrorResponse("Invalid Values Provided",ex.getErrors());
    }

    @ExceptionHandler(NumberFormatException.class)
    public void handleNumberFormatException(NumberFormatException ex){
        throw new PageNumberException();
    }

    @ExceptionHandler(PageNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GenericResponse pageNumberException(PageNumberException ex){
        return new GenericResponse(ex.getMessage());
    }

    @ExceptionHandler (AuthenticationError.class)
    @ResponseStatus (HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public GenericResponse handleAuthenticationError(AuthenticationError ex){
        return new GenericResponse(ex.getMessage());
    }

    @ExceptionHandler (HttpMessageNotReadableException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GenericResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        return new GenericResponse("Invalid JSON Provided");
    }

    @ExceptionHandler (MissingServletRequestParameterException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GenericResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException ex){
        return new GenericResponse(ex.getMessage().split("for")[0].trim());
    }

    @ExceptionHandler (MethodArgumentTypeMismatchException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GenericResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        return new GenericResponse("Invalid Data Type for Parameter");
    }
}
