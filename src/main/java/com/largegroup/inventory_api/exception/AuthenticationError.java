package com.largegroup.inventory_api.exception;

import lombok.Getter;

@Getter
public class AuthenticationError extends RuntimeException {


    public AuthenticationError() {
        super("Action is Not allowed For Current User Role");
    }
}
