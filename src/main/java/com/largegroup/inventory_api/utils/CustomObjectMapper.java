package com.largegroup.inventory_api.utils;

import com.largegroup.inventory_api.model.Category;
import com.largegroup.inventory_api.model.Product;
import com.largegroup.inventory_api.view.CategoryDto;
import com.largegroup.inventory_api.view.ProductDto;

public class CustomObjectMapper {

    public static ProductDto mapProductToDto(Product product){

        return new ProductDto(
                String.valueOf(product.getId()),
                String.valueOf(product.getName()),
                String.valueOf(product.getCategoryId()),
                String.valueOf(product.getPrice()),
                String.valueOf(product.getQuantity())
        );
    }

    public static Product mapDtoToProduct(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setCategoryId(Integer.parseInt(productDto.getCategoryId()));
        product.setPrice(Double.parseDouble(productDto.getPrice()));
        product.setQuantity(Integer.parseInt(productDto.getQuantity()));
        return product;
    }

    public static CategoryDto mapCategoryToDto(Category category){
        return new CategoryDto(
                String.valueOf(category.getId()),
                category.getName()
        );
    }

    public static Category mapDtoToCategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }
}
