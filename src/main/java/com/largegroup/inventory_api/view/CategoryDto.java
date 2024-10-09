package com.largegroup.inventory_api.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    Integer id;
    String name;
    Boolean active;
}
