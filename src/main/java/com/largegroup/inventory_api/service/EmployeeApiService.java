package com.largegroup.inventory_api.service;

import com.largegroup.inventory_api.exception.AuthenticationError;
import com.largegroup.inventory_api.exception.ValidationError;
import com.largegroup.inventory_api.model.Category;
import com.largegroup.inventory_api.model.Product;
import com.largegroup.inventory_api.model.User;
import com.largegroup.inventory_api.repository.CategoryRepository;
import com.largegroup.inventory_api.repository.ProductRepository;
import com.largegroup.inventory_api.repository.UserRepository;
import com.largegroup.inventory_api.utils.CustomObjectMapper;
import com.largegroup.inventory_api.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class EmployeeApiService implements EmployeeApiServiceFunctions{

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public GenericResponse addProductToInventory(ProductDto productDto, Integer userId) {

        String DEFAULT_ACCESS_ROLE = "ADMIN";
        validateUser(userId,DEFAULT_ACCESS_ROLE);

        List<String> errors = validateProduct(productDto);
        if (!errors.isEmpty()) {
            throw new ValidationError("Invalid Fields Provided", errors);
        }
        Product product = productRepository.save(CustomObjectMapper.mapDtoToProduct(productDto));
        return new GenericResponse("Product created successfully with id " + String.valueOf(product.getId()));
    }


    @Override
    public ProductList getProductFromInventory(Integer productId, Integer categoryId, int page) {

        int PAGE_SIZE=25;
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
    public GenericResponse addCategoryToInventory(CategoryDto categoryDto, Integer userId) {

        String DEFAULT_ACCESS_ROLE = "ADMIN";
        validateUser(userId,DEFAULT_ACCESS_ROLE);
        validateCategory(categoryDto);
        Category category = categoryRepository.save(CustomObjectMapper.mapDtoToCategory(categoryDto));
        return new GenericResponse("Category created successfully with id " + String.valueOf(category.getId()));
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

    private void validateUser(Integer userId, String defaultAccessRole) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ValidationError("Invalid User ID",List.of("Invalid User ID Found While Validating"));
        }
        if(defaultAccessRole.equals("ADMIN")){
            if(!user.getRole().equalsIgnoreCase("ADMIN")){
                throw new AuthenticationError();
            }
        }
        else {
            if(!user.getRole().equalsIgnoreCase("CUSTOMER")){
                throw new AuthenticationError();
            }
        }
    }

    private List<String> validateProduct(ProductDto productDto) {

        List<String> errors = new ArrayList<>();

        Integer categoryId;
        try {
            categoryId = productDto.getCategoryId();
        } catch (Exception ex) {
            errors.add("Category ID Should be Number");
            categoryId = null;
        }

        if (categoryId != null) {
            if (categoryId < 1)
                errors.add("Category ID should be greater than 0");
        }

        Double price;
        try {
            price = productDto.getPrice();
        } catch (Exception ex) {

            price = null;
        }

        if (price != null) {
            if (price <= 0)
                errors.add("Price should be greater than 0");
        }
        else {
            errors.add("Invalid Price provided");
        }

        Integer quantity;

        try {
            quantity = productDto.getQuantity();
        } catch (Exception ex) {
            quantity = null;
        }

        if (quantity != null) {
            if (quantity < 0)
                errors.add("Quantity should be greater than 0");
        }
        else {
            errors.add("Invalid Quantity provided");
        }

        if(!errors.isEmpty())
            return errors;

        if(categoryId!=null){
            if (!categoryRepository.existsById(categoryId)){
                errors.add("Invalid Category ID Provided");
                return errors;
            }

            if (productRepository.existsByNameAndCategoryId(productDto.getName(),categoryId))
                errors.add("Product Already Exists With Given name and Category ID");
        }
        return errors;
    }

    private void validateCategory(CategoryDto categoryDto) {
        if(categoryRepository.existsByName(categoryDto.getName()))
            throw new ValidationError("", List.of("Category Already Exists"));
    }
}
