package com.largegroup.inventory_api.utils;

import com.largegroup.inventory_api.model.Category;
import com.largegroup.inventory_api.model.Order;
import com.largegroup.inventory_api.model.Product;
import com.largegroup.inventory_api.view.CategoryDto;
import com.largegroup.inventory_api.view.OrderDto;
import com.largegroup.inventory_api.view.ProductDto;
import org.aspectj.weaver.ast.Or;

public class CustomObjectMapper {

    public static ProductDto mapProductToDto(Product product){

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getPrice(),
                product.getQuantity()
        );
    }

    public static Product mapDtoToProduct(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setCategoryId(productDto.getCategoryId());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        return product;
    }

    public static CategoryDto mapCategoryToDto(Category category){
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category mapDtoToCategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    public static OrderDto mapOrderToDto(Order order){
        OrderDto orderDto = new OrderDto();

        orderDto.setInvoiceId(order.getId());
        orderDto.setUserId(order.getUserId());
        orderDto.setProductId(order.getProductId());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setTotal(order.getTotal());

        return orderDto;
    }
}
