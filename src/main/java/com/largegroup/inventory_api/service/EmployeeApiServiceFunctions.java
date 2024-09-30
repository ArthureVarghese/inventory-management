package com.largegroup.inventory_api.service;

import com.largegroup.inventory_api.view.GenericResponse;
import com.largegroup.inventory_api.view.ProductDto;

public interface EmployeeApiServiceFunctions {

    public GenericResponse addProductToInventory(ProductDto productDto);

    public GenericResponse getProductFromInventory();

    public GenericResponse updateProductInInventory();

    public GenericResponse deleteProductFromInventory();

    public GenericResponse addCategoryToInventory();

    public GenericResponse getCategoryFromInventory();

    public GenericResponse updateCategoryInInventory();

    public GenericResponse deleteCategoryFromInventory();

    public GenericResponse createOrder();
}
