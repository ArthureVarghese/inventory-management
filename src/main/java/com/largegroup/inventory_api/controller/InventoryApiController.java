package com.largegroup.inventory_api.controller;

import com.largegroup.inventory_api.exception.PageNumberException;
import com.largegroup.inventory_api.service.InventoryServiceFunctions;
import com.largegroup.inventory_api.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping (path = "/api/v1", produces = "application/json")
public class InventoryApiController {

    @Autowired
    InventoryServiceFunctions inventoryApiService;

    @PostMapping (path = "/product", produces = "application/json")
    @ResponseStatus (HttpStatus.CREATED)
    @ResponseBody
    public GenericResponse addProduct(@RequestBody ProductDto productDto, @RequestParam (name = "user-id") Integer userId) {
        return inventoryApiService.addProductToInventory(productDto, userId);
    }


    @GetMapping (path = "/product", produces = "application/json")
    @ResponseStatus (HttpStatus.OK)
    @ResponseBody
    public ProductList getProduct(
            @RequestParam (name = "product-id", required = false) Integer productId,
            @RequestParam (name = "category-id", required = false) Integer categoryId,
            @RequestParam (name = "active" , required = false, defaultValue = "true") Boolean active,
            @RequestParam (name = "page", required = false, defaultValue = "1") String page) {
        return inventoryApiService.getProductFromInventory(productId, categoryId, parseAndValidatePageNumber(page),active);
    }

    @PutMapping (path = "/product", produces = "application/json")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void updateProduct(
            @RequestParam (name = "product-id", required = true) Integer productId,
            @RequestParam (name = "product-name", required = false) String productName,
            @RequestParam (name = "category-id", required = false) Integer categoryId,
            @RequestParam (name = "price", required = false) Double price,
            @RequestParam (name = "quantity", required = false) Integer quantity,
            @RequestParam (name = "active" , required = false) Boolean active,
            @RequestParam (name = "user-id", required = true) Integer userId) {
        inventoryApiService.updateProductInInventory(productId, productName, categoryId, price, quantity, userId, active);
    }


    @PostMapping (path = "/category", produces = "application/json")
    @ResponseStatus (HttpStatus.CREATED)
    @ResponseBody
    public GenericResponse addCategory(@RequestBody CategoryDto categoryDto, @RequestParam (name = "user-id") Integer userId) {
        return inventoryApiService.addCategoryToInventory(categoryDto, userId);
    }

    @GetMapping (path = "/category", produces = "application/json")
    @ResponseStatus (HttpStatus.OK)
    @ResponseBody
    public CategoryList getCategory(
            @RequestParam (name = "category-id", required = false) Integer categoryId,
            @RequestParam (name = "active" , required = false, defaultValue = "true") Boolean active,
            @RequestParam (name = "page", required = false, defaultValue = "1") String page) {
        return inventoryApiService.getCategoryFromInventory(categoryId, parseAndValidatePageNumber(page),active);
    }

    @PutMapping (path = "/category", produces = "application/json")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void updateCategory(
            @RequestParam (name = "category-id", required = true) Integer categoryId,
            @RequestParam (name = "name", required = false) String name,
            @RequestParam (name = "user-id", required = true) Integer userId,
            @RequestParam (name = "active", required = false) Boolean active) {
        inventoryApiService.updateCategoryInInventory(categoryId, name, userId, active);
    }

    @PutMapping (path = "/order", produces = "application/json")
    @ResponseStatus (HttpStatus.CREATED)
    @ResponseBody
    public OrderDto createOrder(@RequestParam (name = "user-id") Integer userId,
                                @RequestParam (name = "product-id") Integer productId,
                                @RequestParam (name = "quantity") Integer quantity) {
        return inventoryApiService.createOrder(productId, userId, quantity);
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
