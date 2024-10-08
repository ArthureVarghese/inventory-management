package com.largegroup.inventory_api.service;

import com.largegroup.inventory_api.cache.annotation.CacheDelete;
import com.largegroup.inventory_api.cache.annotation.Cached;
import com.largegroup.inventory_api.exception.AuthenticationError;
import com.largegroup.inventory_api.exception.OrderCreationError;
import com.largegroup.inventory_api.exception.ValidationError;
import com.largegroup.inventory_api.model.Category;
import com.largegroup.inventory_api.model.Order;
import com.largegroup.inventory_api.model.Product;
import com.largegroup.inventory_api.model.User;
import com.largegroup.inventory_api.repository.CategoryRepository;
import com.largegroup.inventory_api.repository.OrderRepository;
import com.largegroup.inventory_api.repository.ProductRepository;
import com.largegroup.inventory_api.repository.UserRepository;
import com.largegroup.inventory_api.utils.CustomObjectMapper;
import com.largegroup.inventory_api.view.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class InventoryApiService implements InventoryServiceFunctions {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Override
    @CacheDelete
    public GenericResponse addProductToInventory(ProductDto productDto, Integer userId) {

        String DEFAULT_ACCESS_ROLE = "ADMIN";
        validateUser(userId, DEFAULT_ACCESS_ROLE);

        List<String> errors = validateProduct(productDto);
        if (!errors.isEmpty()) {
            throw new ValidationError(errors);
        }

        Product product = productRepository.save(CustomObjectMapper.mapDtoToProduct(productDto));

        return new GenericResponse("Product created successfully with id " + product.getId());
    }


    @Override
    @Cached
    public ProductList getProductFromInventory(Integer productId, Integer categoryId, int page, Boolean active) {

        int PAGE_SIZE = 25;
        Pageable pageRequest = PageRequest.of(page, PAGE_SIZE);
        List<Product> products;

        if (productId != null && categoryId != null) {
            products = productRepository.findByIdAndCategoryIdAndActive(productId, categoryId, active, pageRequest);
            return new ProductList(products);
        } else if (productId != null) {
            products = productRepository.findByIdAndActive(productId, active, pageRequest);
            return new ProductList(products);
        } else if (categoryId != null) {
            products = productRepository.findByCategoryIdAndActive(categoryId,active, pageRequest);
            return new ProductList(products);
        }
        products = productRepository.findByActive(active,pageRequest);
        return new ProductList(products);

    }

    @Override
    @CacheDelete
    @Transactional
    public void updateProductInInventory(Integer productId, String productName, Integer categoryId, Double price, Integer quantity, Integer userId, Boolean active) {

        if (productName == null && categoryId == null && price == null && quantity == null && active == null)
            throw new ValidationError(List.of("No parameters provided"));

        String DEFAULT_ACCESS_ROLE = "ADMIN";
        validateUser(userId, DEFAULT_ACCESS_ROLE);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationError(List.of("No Product Found with the Given Product-id")));

        if (productName != null) {

            if (productName.equalsIgnoreCase(product.getName()))
                throw new ValidationError(List.of("Can't change to same product name"));

            product.setName(productName);
        }

        if (categoryId != null) {

            if (categoryId.equals(product.getCategoryId()))
                throw new ValidationError(List.of("Can't change to same category id"));

            if (!categoryRepository.existsByIdAndActiveIsTrue(categoryId))
                throw new ValidationError(List.of("Category is either non existent or Inactive"));

            product.setCategoryId(categoryId);
        }


        if (price != null) {
            price = Math.round(price * 100.00) / 100.00;

            if (price.equals(product.getPrice()))
                throw new ValidationError(List.of("Can't change to same price"));

            if (price <= 0)
                throw new ValidationError(List.of("Price should be greater than 0"));

            product.setPrice(price);
        }

        if (quantity != null) {

            if (quantity.equals(product.getQuantity()))
                throw new ValidationError(List.of("Can't change to same quantity"));

            if (quantity <= 0)
                throw new ValidationError(List.of("Quantity should be greater than 0"));

            product.setQuantity(quantity);
        }

        if (active != null) {

            if (active.equals(product.getActive()))
                throw new ValidationError(List.of("Can't change to the same active status"));

            if (active.equals(Boolean.TRUE)) {
                if (!categoryRepository.existsByIdAndActiveIsTrue(product.getCategoryId()))
                    throw new ValidationError(List.of("Can't change product status to Active because Category is Inactive"));
            }

            product.setActive(active);
        }

        productRepository.save(product);

    }


    @Override
    @CacheDelete
    public GenericResponse addCategoryToInventory(CategoryDto categoryDto, Integer userId) {

        String DEFAULT_ACCESS_ROLE = "ADMIN";
        validateUser(userId, DEFAULT_ACCESS_ROLE);
        List<String> errors= new ArrayList<>();


        if(categoryDto.getName()==null){
            errors.add("Name cannot be null");
        }

        if(categoryDto.getActive()==null){
            errors.add("Active status cannot be null");
        }

        if(!errors.isEmpty())
            throw new ValidationError(errors);

        if (categoryRepository.existsByName(categoryDto.getName()))
            throw new ValidationError(List.of("Category Already Exists"));

        Category category = categoryRepository.save(CustomObjectMapper.mapDtoToCategory(categoryDto));
        return new GenericResponse("Category created successfully with id " + category.getId());
    }


    @Override
    @Cached
    public CategoryList getCategoryFromInventory(Integer categoryId, int page, Boolean active) {
        Pageable pageRequest = PageRequest.of(page, 25);

        if (categoryId == null) {
            Page<Category> categories = categoryRepository.findByActive(active,pageRequest);
            return new CategoryList(categories.getContent());
        }

        List<Category> categories = categoryRepository.findByIdAndActive(categoryId, active, pageRequest);
        return new CategoryList(categories);
    }

    @Override
    @CacheDelete
    public void updateCategoryInInventory(Integer categoryId, String name, Integer userId, Boolean active) {

        if (name == null && active == null)
            throw new ValidationError(List.of("No parameters provided"));


        String DEFAULT_ACCESS_ROLE = "ADMIN";
        validateUser(userId, DEFAULT_ACCESS_ROLE);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ValidationError(List.of("No category found with the given id")));

        if (name != null) {
            if (category.getName().equalsIgnoreCase(name))
                throw new ValidationError(List.of("Can't change to the Same Category name"));
            if (categoryRepository.existsByName(name))
                throw new ValidationError(List.of("Category with the same name already exists"));
            category.setName(name);
        }

        if (active != null) {
            if (productRepository.existsByCategoryIdAndActiveIsTrue(categoryId))
                throw new ValidationError(List.of("Cannot Change Active status. There are Active products in this Category"));
            category.setActive(active);
        }

        categoryRepository.save(category);

    }


    @Override
    @Transactional
    public OrderDto createOrder(Integer productId, Integer userId, Integer quantity) {

        String DEFAULT_ACCESS_ROLE = "CUSTOMER";
        validateUser(userId, DEFAULT_ACCESS_ROLE);

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null)
            throw new OrderCreationError("No Product Found With Given ID");

        if (product.getActive().equals(Boolean.FALSE))
            throw new OrderCreationError("Inactive Product Provided");

        if (quantity < 1)
            throw new OrderCreationError("Quantity Should be greater than 0");

        if (product.getQuantity() < quantity)
            throw new OrderCreationError("Cannot Create Order! Not enough Quantity available");

        product.setQuantity(product.getQuantity() - quantity);

        Order order = new Order();

        order.setUserId(userId);
        order.setProductId(productId);
        order.setPrice(product.getPrice());
        order.setQuantity(quantity);
        order.setTotal(Math.round(quantity * product.getPrice() * 100.0) / 100.0);

        order = orderRepository.save(order);
        productRepository.save(product);

        return CustomObjectMapper.mapOrderToDto(order);
    }

    private void validateUser(Integer userId, String defaultAccessRole) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ValidationError(List.of("Invalid User ID Found While Validating"));
        }
        if (defaultAccessRole.equals("ADMIN")) {
            if (!user.getRole().equalsIgnoreCase("ADMIN")) {
                throw new AuthenticationError();
            }
        } else {
            if (!user.getRole().equalsIgnoreCase("CUSTOMER")) {
                throw new AuthenticationError();
            }
        }
    }

    private List<String> validateProduct(ProductDto productDto) {

        List<String> errors = new ArrayList<>();

        if (productDto.getCategoryId() != null) {
            if (productDto.getCategoryId() < 1)
                errors.add("Category ID should be greater than 0");
        } else {
            errors.add("No Category ID provided");
        }


        if (productDto.getPrice() != null) {
            if (productDto.getPrice() <= 0)
                errors.add("Price should be greater than 0");
        } else {
            errors.add("No Price Provided");
        }

        if (productDto.getQuantity() != null) {
            if (productDto.getQuantity() < 0)
                errors.add("Quantity should be greater than 0");
        } else {
            errors.add("No Quantity Provided");
        }

        if (productDto.getActive() == null) {
            errors.add("value for active is not Provided");
        }

        if (!errors.isEmpty())
            return errors;

        if (!categoryRepository.existsByIdAndActiveIsTrue(productDto.getCategoryId())) {
            errors.add("Provided Category ID is either non existing or Not active");
            return errors;
        }

        if (productRepository.existsByNameAndCategoryId(productDto.getName(), productDto.getCategoryId()))
            errors.add("Product Already Exists With Given name and Category ID");

        return errors;
    }
}
