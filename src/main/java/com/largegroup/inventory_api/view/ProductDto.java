package com.largegroup.inventory_api.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDto {
    Integer id;
    String name;
    Integer categoryId;
    Double price;
    Integer quantity;
}
