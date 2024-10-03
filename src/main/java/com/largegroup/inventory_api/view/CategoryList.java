package com.largegroup.inventory_api.view;

import com.largegroup.inventory_api.model.Category;
import com.largegroup.inventory_api.utils.CustomObjectMapper;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoryList {
    List<CategoryDto> categories;

    public CategoryList(List<Category> categories) {
        this.categories = categories.stream()
                .map(CustomObjectMapper::mapCategoryToDto)
                .collect(Collectors.toList());
    }
}

