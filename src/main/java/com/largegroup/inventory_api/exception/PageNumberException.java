package com.largegroup.inventory_api.exception;

public class PageNumberException extends RuntimeException{
    public PageNumberException(String message) {
        super("Invalid Value detected : " + message);
    }
}

