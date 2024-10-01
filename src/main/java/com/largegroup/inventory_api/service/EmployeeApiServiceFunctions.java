package com.largegroup.inventory_api.service;

import com.largegroup.inventory_api.view.*;

public interface EmployeeApiServiceFunctions {

    public GenericResponse addProductToInventory(ProductDto productDto, Integer userId);

    public ProductList getProductFromInventory(Integer productId, Integer categoryId, int page);

    public GenericResponse updateProductInInventory();

    public void deleteProductFromInventory(Integer productId, Integer userId);

    public GenericResponse addCategoryToInventory(CategoryDto categoryDto, Integer userId);

    public CategoryList getCategoryFromInventory(Integer categoryId, int page);

    public GenericResponse updateCategoryInInventory();

    public void deleteCategoryFromInventory(Integer categoryId, Integer userId);

    public GenericResponse createOrder();
}
