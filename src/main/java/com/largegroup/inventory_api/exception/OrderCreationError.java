package com.largegroup.inventory_api.exception;

public class OrderCreationError extends RuntimeException {
    public OrderCreationError(String message) {
        super(message);
    }
}
