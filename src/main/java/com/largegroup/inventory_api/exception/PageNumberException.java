package com.largegroup.inventory_api.exception;

public class PageNumberException extends RuntimeException {
    public PageNumberException() {
        super("Invalid Page Number Provided");
    }
}

