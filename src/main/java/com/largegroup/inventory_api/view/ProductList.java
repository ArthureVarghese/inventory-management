package com.largegroup.inventory_api.view;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.largegroup.inventory_api.model.Product;
import com.largegroup.inventory_api.utils.CustomObjectMapper;

@Data
public class ProductList {
    List<ProductDto> products;

    public ProductList(List<Product> products){
            this.products = Collections.unmodifiableList(products.stream()
                .map(CustomObjectMapper::mapProductToDto)
                .collect(Collectors.toList()));
        
    }
}
