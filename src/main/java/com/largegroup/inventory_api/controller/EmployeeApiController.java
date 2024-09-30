package com.largegroup.inventory_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping (path = "/api/v1", produces = "application/json")
public class EmployeeApiController {

    @PostMapping (path = "/product", produces = "application/json")
    @ResponseStatus (HttpStatus.CREATED)
    public void addProduct() {
    }

    @GetMapping (path = "/product", produces = "application/json")
    @ResponseStatus (HttpStatus.OK)
    public void getProduct() {
    }

    @PutMapping (path = "/product", produces = "application/json")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void updateProduct() {
    }

    @DeleteMapping (path = "/product", produces = "application/json")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deleteProduct() {
    }

    @PostMapping (path = "/category", produces = "application/json")
    @ResponseStatus (HttpStatus.CREATED)
    public void addCategory() {
    }

    @GetMapping (path = "/category", produces = "application/json")
    @ResponseStatus (HttpStatus.OK)
    public void getCategory() {
    }

    @PutMapping (path = "/category", produces = "application/json")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void updateCategory() {
    }

    @DeleteMapping (path = "/category", produces = "application/json")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deleteCategory() {
    }

    @PutMapping (path = "/order", produces = "application/json")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void createOrder() {
    }
}
