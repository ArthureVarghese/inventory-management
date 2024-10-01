package com.largegroup.inventory_api.service;

import com.largegroup.inventory_api.view.CategoryList;
import com.largegroup.inventory_api.view.GenericResponse;
import com.largegroup.inventory_api.view.ProductDto;
import com.largegroup.inventory_api.view.ProductList;

public interface EmployeeApiServiceFunctions {

    public GenericResponse addProductToInventory(ProductDto productDto);

    public ProductList getProductFromInventory(Integer productId, Integer categoryId, int page);

    public GenericResponse updateProductInInventory();

    public GenericResponse deleteProductFromInventory();

    public GenericResponse addCategoryToInventory();

    public CategoryList getCategoryFromInventory(Integer categoryId, int page);

    public GenericResponse updateCategoryInInventory();

    public GenericResponse deleteCategoryFromInventory();

    public GenericResponse createOrder();
}
