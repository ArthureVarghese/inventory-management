package com.largegroup.inventory_api.exception;

import com.largegroup.inventory_api.view.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler (ValidationError.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse pageNumberException(ValidationError ex){
        return new ErrorResponse("Invalid Value for Page Number",ex.getErrors());
    }
}
