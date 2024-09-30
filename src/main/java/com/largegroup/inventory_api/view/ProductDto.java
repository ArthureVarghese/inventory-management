package com.largegroup.inventory_api.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDto {
    String id;
    String name;
    String categoryId;
    String price;
    String quantity;
}
