package com.largegroup.inventory_api.controller;

import com.largegroup.inventory_api.exception.PageNumberException;
import com.largegroup.inventory_api.service.EmployeeApiServiceFunctions;
import com.largegroup.inventory_api.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping (path = "/api/v1", produces = "application/json")
public class EmployeeApiController {

    @Autowired
    EmployeeApiServiceFunctions employeeApiService;

    @PostMapping (path = "/product", produces = "application/json")
    @ResponseStatus (HttpStatus.CREATED)
    @ResponseBody
    public GenericResponse addProduct(@RequestBody ProductDto productDto, @RequestParam(name = "user-id") Integer userId) {
        return employeeApiService.addProductToInventory(productDto,userId);
    }


    @GetMapping (path = "/product", produces = "application/json")
    @ResponseStatus (HttpStatus.OK)
    @ResponseBody
    public ProductList getProduct(
        @RequestParam (name = "product-id", required = false) Integer productId,
        @RequestParam (name = "category-id", required = false) Integer categoryId,
        @RequestParam (name = "page", required = false, defaultValue = "1") String page)
    {
        return employeeApiService.getProductFromInventory(productId,categoryId,parseAndValidatePageNumber(page));
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
    @ResponseBody
    public GenericResponse addCategory(@RequestBody CategoryDto categoryDto,@RequestParam(name = "user-id") Integer userId) {
        return employeeApiService.addCategoryToInventory(categoryDto,userId);
    }

    @GetMapping (path = "/category", produces = "application/json")
    @ResponseStatus (HttpStatus.OK)
    @ResponseBody
    public CategoryList getCategory(
        @RequestParam (name = "category-id", required = false) Integer categoryId,
        @RequestParam (name = "page", required = false, defaultValue = "1") String page)
    {
        return employeeApiService.getCategoryFromInventory(categoryId,parseAndValidatePageNumber(page));
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

    // Throws error if page number is non-numeric or less than 0
    private int parseAndValidatePageNumber(String pageNumber) {
        int number = Integer.parseInt(pageNumber);
        // zero based index adjustment
        number -= 1;
        if (number < 0) throw new PageNumberException();
        return number;
    }
}
