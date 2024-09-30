package com.largegroup.inventory_api.view;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    String message;
    List<String> errors;
}
