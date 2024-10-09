package com.largegroup.inventory_api.service;

import com.largegroup.inventory_api.view.*;

public interface EmployeeApiServiceFunctions {

    public GenericResponse addProductToInventory(ProductDto productDto, Integer userId);

    public ProductList getProductFromInventory(Integer productId, Integer categoryId, int page);

    public void updateProductInInventory(Integer productId, String productName, Integer categoryId, Double price, Integer quantity, Integer userId, Boolean active);

    public GenericResponse addCategoryToInventory(CategoryDto categoryDto, Integer userId);

    public CategoryList getCategoryFromInventory(Integer categoryId, int page);

    public void updateCategoryInInventory(Integer categoryId, String name, Integer userId, Boolean active);

    public OrderDto createOrder(Integer productId, Integer userId, Integer quantity);
}
