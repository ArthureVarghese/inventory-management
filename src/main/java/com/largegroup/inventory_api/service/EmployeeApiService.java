package com.largegroup.inventory_api.service;

import java.util.ArrayList;
import java.util.List;

import com.largegroup.inventory_api.exception.ValidationError;
import com.largegroup.inventory_api.model.Category;
import com.largegroup.inventory_api.model.Product;
import com.largegroup.inventory_api.repository.CategoryRepository;
import com.largegroup.inventory_api.repository.ProductRepository;
import com.largegroup.inventory_api.utils.CustomObjectMapper;
import com.largegroup.inventory_api.view.CategoryList;
import com.largegroup.inventory_api.view.GenericResponse;
import com.largegroup.inventory_api.view.ProductDto;
import com.largegroup.inventory_api.view.ProductList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class EmployeeApiService implements EmployeeApiServiceFunctions{

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public GenericResponse addProductToInventory(ProductDto productDto) {
        List<String> errors = validateProduct(productDto);
        if (!errors.isEmpty()) {
            throw new ValidationError("Invalid Fields Provided", errors);
        }
        productRepository.save(CustomObjectMapper.mapDtoToProduct(productDto));
        return new GenericResponse("Product created successfully");
    }

    @Override
    public ProductList getProductFromInventory(Integer productId, Integer categoryId, int page) {

        Integer PAGE_SIZE=25;
        Pageable pageRequest = PageRequest.of(page,PAGE_SIZE);
        List<Product> products;

        if(productId!=null && categoryId!=null)
        {
            products=productRepository.findByIdAndCategoryId(productId,categoryId,pageRequest);
            return new ProductList(products);
        }
        else if(productId!=null)
        {
            products=productRepository.findById(productId,pageRequest);
            return new ProductList(products);
        }
        else if(categoryId!=null)
        {
            products=productRepository.findByCategoryId(categoryId,pageRequest);
            return new ProductList(products);
        } 

        Page<Product> product = productRepository.findAll(pageRequest);
        return new ProductList(product.getContent());

    }

    @Override
    public GenericResponse updateProductInInventory() {
        return null;
    }

    @Override
    public GenericResponse deleteProductFromInventory() {
        return null;
    }

    @Override
    public GenericResponse addCategoryToInventory() {
        return null;
    }

    @Override
    public CategoryList getCategoryFromInventory(Integer categoryId, int page) {
        Pageable pageRequest = PageRequest.of(page, 25);

        if (categoryId == null) {
            Page<Category> categories = categoryRepository.findAll(pageRequest);
            return new CategoryList(categories.getContent());
        }

        List<Category> employees = categoryRepository.findById(categoryId,pageRequest);
        return new CategoryList(employees);
    }

    @Override
    public GenericResponse updateCategoryInInventory() {
        return null;
    }

    @Override
    public GenericResponse deleteCategoryFromInventory() {
        return null;
    }

    @Override
    public GenericResponse createOrder() {
        return null;
    }

    private List<String> validateProduct(ProductDto productDto) {

        List<String> errors = new ArrayList<>();

        //TODO: Need to add a db check also
        Integer categoryId;

        try {
            categoryId = Integer.parseInt(productDto.getCategoryId());
        } catch (Exception ex) {
            errors.add("Category ID Should be Number");
            categoryId = null;
        }

        if (categoryId != null) {
            if (categoryId < 1)
                errors.add("Category ID should be greater than 0");

            if (!categoryRepository.existsById(categoryId))
                errors.add("Invalid Category ID Provided");
        }
        Double price;
        try {
            price = Double.parseDouble(productDto.getPrice());
        } catch (Exception ex) {
            errors.add("Invalid Price provided");
            price = null;
        }

        if (price != null) {
            if (price <= 0)
                errors.add("Price should be greater than 0");
        }

        Integer quantity;

        try {
            quantity = Integer.parseInt(productDto.getQuantity());
        } catch (Exception ex) {
            errors.add("Invalid Quantity provided");
            quantity = null;
        }

        if (quantity != null) {
            if (quantity < 0)
                errors.add("Quantity should be greater than 0");
        }

        return errors;
    }

}
