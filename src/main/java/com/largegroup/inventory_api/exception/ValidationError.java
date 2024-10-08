package com.largegroup.inventory_api.exception;

import lombok.Getter;

import java.util.List;


@Getter
public class ValidationError extends RuntimeException {

    private final List<String> errors;

    public ValidationError(List<String> errors) {
        super("Invalid Value detected");
        this.errors = errors;
    }
}
