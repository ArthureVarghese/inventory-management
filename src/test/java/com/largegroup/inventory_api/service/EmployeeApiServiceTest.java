package com.largegroup.inventory_api.service;

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
import com.largegroup.inventory_api.view.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EmployeeApiServiceTest {

    Product mockProduct;
    Category mockCategory;
    User mockUser;
    @InjectMocks
    private EmployeeApiService employeeApiService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockProduct = new Product();
        mockCategory = new Category();
        mockUser = new User();
    }

    @Test
    void testGetProductFromInventory_WithAllParams() {

        List<Product> products = Collections.singletonList(mockProduct);
        when(productRepository.findByIdAndCategoryId(1, 1, PageRequest.of(1, 25))).thenReturn(products);

        ProductList response = employeeApiService.getProductFromInventory(1, 1, 1);

        assertThat(response).isNotNull();
        assertThat(response.getProducts().size()).isEqualTo(1);

    }

    @Test
    void testGetProductFromInventory_WithNoParams() {

        List<Product> products = Collections.singletonList(mockProduct);
        when(productRepository.findAll(PageRequest.of(0, 25))).thenReturn(new PageImpl<>(products));

        ProductList response = employeeApiService.getProductFromInventory(null, null, 0);

        assertThat(response).isNotNull();
        assertThat(response.getProducts().size()).isEqualTo(1);

    }

    @Test
    void testGetProductFromInventory_WithOnlyCategoryId() {

        List<Product> products = Collections.singletonList(mockProduct);
        when(productRepository.findByCategoryId(1, PageRequest.of(0, 25))).thenReturn(products);

        ProductList response = employeeApiService.getProductFromInventory(null, 1, 0);

        assertThat(response).isNotNull();
        assertThat(response.getProducts().size()).isEqualTo(1);

    }

    @Test
    void testGetProductFromInventory_WithOnlyProductId() {

        List<Product> products = Collections.singletonList(mockProduct);
        when(productRepository.findById(1, PageRequest.of(0, 25))).thenReturn(products);

        ProductList response = employeeApiService.getProductFromInventory(1, null, 0);

        assertThat(response).isNotNull();
        assertThat(response.getProducts().size()).isEqualTo(1);

    }

    @Test
    void testGetCategoryFromInventory_WithCategoryId() {

        when(categoryRepository.findById(1, PageRequest.of(0, 25))).thenReturn(Collections.singletonList(mockCategory));

        CategoryList response = employeeApiService.getCategoryFromInventory(1, 0);

        assertThat(response).isNotNull();
        assertThat(response.getCategories().size()).isEqualTo(1);

    }

    @Test
    void testGetCategoryFromInventory_WithNoParam() {

        when(categoryRepository.findAll(PageRequest.of(0, 25))).thenReturn(new PageImpl<>(Collections.singletonList(mockCategory)));

        CategoryList response = employeeApiService.getCategoryFromInventory(null, 0);

        assertThat(response).isNotNull();
        assertThat(response.getCategories().size()).isEqualTo(1);

    }

    @Test
    void testUpdateProductInInventory_ValidAllParam() {

        mockUser.setRole("Admin");

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(categoryRepository.existsById(1)).thenReturn(true);

        employeeApiService.updateProductInInventory(1, "Product", 1, 10.0, 1, 1);

        assertThat(mockProduct.getName()).isEqualTo("Product");
        assertThat(mockProduct.getCategoryId()).isEqualTo(1);
        assertThat(mockProduct.getPrice()).isEqualTo(10.0);
        assertThat(mockProduct.getQuantity()).isEqualTo(1);

    }

    @Test
    void testUpdateProductInInventory_NoParam() {

        ValidationError exception = assertThrows(ValidationError.class, () -> employeeApiService.updateProductInInventory(1, null, null, null, null, 1));
        assertThat(exception.getErrors().getFirst()).isEqualTo("No parameters provided");

    }

    @Test
    void testUpdateProductInInventory_InvalidUserId() {

        ValidationError exception = assertThrows(ValidationError.class, () -> employeeApiService.updateProductInInventory(1, "Product", null, null, null, 1));
        assertThat(exception.getErrors().getFirst()).isEqualTo("Invalid User ID Found While Validating");

    }

    @Test
    void testUpdateProductInInventory_UserNotAnAdmin() {

        mockUser.setRole("Buyer");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        Exception exception = assertThrows(AuthenticationError.class, () -> employeeApiService.updateProductInInventory(1, "Product", null, null, null, 1));
        assertThat(exception.getMessage()).isEqualTo("Action is Not allowed For Current User Role");

    }

    @Test
    void testUpdateProductInInventory_InvalidProductId() {

        mockUser.setRole("Admin");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        ValidationError exception = assertThrows(ValidationError.class, () -> employeeApiService.updateProductInInventory(1, "Product", null, null, null, 1));
        assertThat(exception.getErrors().getFirst()).isEqualTo("No Product Found with the Given Product-id");

    }

    @Test
    void testUpdateProductInInventory_SameProductName() {

        mockUser.setRole("Admin");
        mockProduct.setName("product");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        ValidationError exception = assertThrows(ValidationError.class, () -> employeeApiService.updateProductInInventory(1, "Product", null, null, null, 1));
        assertThat(exception.getErrors().getFirst()).isEqualTo("Can't change to same product name");

    }

    @Test
    void testUpdateProductInInventory_InvalidCategoryId() {

        mockUser.setRole("Admin");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(categoryRepository.existsById(1)).thenReturn(false);

        ValidationError exception = assertThrows(ValidationError.class, () -> employeeApiService.updateProductInInventory(1, "Product", 1, null, null, 1));
        assertThat(exception.getErrors().getFirst()).isEqualTo("No Category Found with the Given Category-id");

        mockProduct.setCategoryId(1);
        exception = assertThrows(ValidationError.class, () -> employeeApiService.updateProductInInventory(1, null, 1, null, null, 1));
        assertThat(exception.getErrors().getFirst()).isEqualTo("Can't change to same category id");

    }

    @Test
    void testUpdateProductInInventory_InvalidPrice() {

        mockUser.setRole("Admin");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        ValidationError exception = assertThrows(ValidationError.class, () -> employeeApiService.updateProductInInventory(1, null, null, 0.0, null, 1));
        assertThat(exception.getErrors().getFirst()).isEqualTo("Price should be greater than 0");

        mockProduct.setPrice(10.0);
        exception = assertThrows(ValidationError.class, () -> employeeApiService.updateProductInInventory(1, null, null, 10.0, null, 1));
        assertThat(exception.getErrors().getFirst()).isEqualTo("Can't change to same price");

    }

    @Test
    void testUpdateProductInInventory_InvalidQuantity() {

        mockUser.setRole("Admin");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        ValidationError exception = assertThrows(ValidationError.class, () -> employeeApiService.updateProductInInventory(1, null, null, null, 0, 1));
        assertThat(exception.getErrors().getFirst()).isEqualTo("Quantity should be greater than 0");

        mockProduct.setQuantity(45);
        exception = assertThrows(ValidationError.class, () -> employeeApiService.updateProductInInventory(1, null, null, null, 45, 1));
        assertThat(exception.getErrors().getFirst()).isEqualTo("Can't change to same quantity");

    }

    @Test
    void testUpdateCategoryInInventory_Valid() {

        mockUser.setRole("Admin");
        mockCategory.setName("Category");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(mockCategory));
        when(categoryRepository.existsByName(any())).thenReturn(false);

        employeeApiService.updateCategoryInInventory(1, "Name", 1, active);

        assertThat(mockCategory.getName()).isEqualTo("Name");

    }

    @Test
    void testUpdateCategoryInInventory_InvalidUser() {

        ValidationError exception = assertThrows(ValidationError.class, () -> employeeApiService.updateCategoryInInventory(1, "A", 1, active));
        assertThat(exception.getErrors().getFirst()).isEqualTo("Invalid User ID Found While Validating");

        mockUser.setRole("Buyer");
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(mockUser));

        Exception exceptionAuthentication = assertThrows(AuthenticationError.class, () -> employeeApiService.updateCategoryInInventory(1, "A", 1, active));
        assertThat(exceptionAuthentication.getMessage()).isEqualTo("Action is Not allowed For Current User Role");

    }

    @Test
    void testUpdateCategoryInInventory_InvalidCategory() {


        // Invalid Category ID
        mockUser.setRole("Admin");
        mockCategory.setName("Category");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        ValidationError exception = assertThrows(ValidationError.class, () -> employeeApiService.updateCategoryInInventory(1, "A", 1, active));
        assertThat(exception.getErrors().getFirst()).isEqualTo("No category found with the given id");


        // Category has the same name as the given name
        mockCategory.setName("Category");
        when(categoryRepository.findById(1)).thenReturn(Optional.of(mockCategory));

        exception = assertThrows(ValidationError.class, () -> employeeApiService.updateCategoryInInventory(1, "Category", 1, active));
        assertThat(exception.getErrors().getFirst()).isEqualTo("Can't change to the Same Category name");


        // Name already exists
        when(categoryRepository.existsByName(any())).thenReturn(true);

        exception = assertThrows(ValidationError.class, () -> employeeApiService.updateCategoryInInventory(1, "A", 1, active));
        assertThat(exception.getErrors().getFirst()).isEqualTo("Category with the same name already exists");
    }


    @Test
    void addProductToInventoryWithInvalidUser() {
        mockUser.setRole("CUSTOMER");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        assertThrows(AuthenticationError.class, () -> employeeApiService.addProductToInventory(new ProductDto(1, "a", 1, 10.0, 10), 1));

    }

    @Test
    void addProductToInventoryWithInvalidCategoryId() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        assertThrows(ValidationError.class, () -> employeeApiService.addProductToInventory(new ProductDto(1, "a", 0, 10.0, 10), 1));
    }

    @Test
    void addProductToInventoryWithInvalidPrice() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        assertThrows(ValidationError.class, () -> employeeApiService.addProductToInventory(new ProductDto(1, "a", 1, -10.0, 10), 1));
    }

    @Test
    void addProductToInventoryWithInvalidQuantity() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        assertThrows(ValidationError.class, () -> employeeApiService.addProductToInventory(new ProductDto(1, "a", 1, 10.0, -10), 1));
    }

    @Test
    void addProductToInventoryWithNonExistentCategoryID() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(categoryRepository.existsById(any())).thenReturn(false);
        assertThrows(ValidationError.class, () -> employeeApiService.addProductToInventory(new ProductDto(1, "a", 1, 10.0, 10), 1));
    }

    @Test
    void addProductToInventoryWithDuplicateProduct() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(categoryRepository.existsById(any())).thenReturn(true);
        when(productRepository.existsByNameAndCategoryId(any(), any())).thenReturn(true);
        assertThrows(ValidationError.class, () -> employeeApiService.addProductToInventory(new ProductDto(1, "a", 1, 10.0, 10), 1));
    }

    @Test
    void addProductToInventory() {
        mockUser.setRole("ADMIN");
        mockProduct.setId(1);
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(categoryRepository.existsById(any())).thenReturn(true);
        when(productRepository.existsByNameAndCategoryId(any(), any())).thenReturn(false);
        when(productRepository.save(any())).thenReturn(mockProduct);
        GenericResponse gr = employeeApiService.addProductToInventory(new ProductDto(1, "a", 1, 10.0, 10), 1);
        assertThat(gr.getMessage()).isEqualTo("Product created successfully with id 1");
    }

    @Test
    void deleteProductFromInventoryWithInvalidUserId() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ValidationError.class, () -> employeeApiService.deleteProductFromInventory(1, 1));
    }

    @Test
    void deleteProductFromInventoryWithInvalidRole() {
        mockUser.setRole("CUSTOMER");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        assertThrows(AuthenticationError.class, () -> employeeApiService.deleteProductFromInventory(1, 1));
    }

    @Test
    void deleteProductFromInventoryWithInvalidProductId() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(productRepository.existsById(any())).thenReturn(false);
        assertThrows(ValidationError.class, () -> employeeApiService.deleteProductFromInventory(1, 1));
    }


    @Test
    void deleteProductFromInventory() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(productRepository.existsById(any())).thenReturn(true);
        when(productRepository.save(any())).thenReturn(mockProduct);
        employeeApiService.deleteProductFromInventory(1,1);
    }

    @Test
    void addCategoryToInventoryWithCategoryAlreadyExists() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(categoryRepository.existsByName(any())).thenReturn(true);
        assertThrows(ValidationError.class, () -> employeeApiService.addCategoryToInventory(new CategoryDto(1,"a"),1));
    }

    @Test
    void addCategoryToInventory() {
        mockUser.setRole("ADMIN");
        mockCategory.setId(1);
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(categoryRepository.existsByName(any())).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(mockCategory);
        GenericResponse gr = employeeApiService.addCategoryToInventory(new CategoryDto(1,"a"),1);
        assertThat(gr.getMessage()).isEqualTo("Category created successfully with id 1");
    }

    @Test
    void deleteCategoryFromInventoryWithInvalidCategoryId() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(categoryRepository.existsById(any())).thenReturn(false);
        assertThrows(ValidationError.class, () -> employeeApiService.deleteCategoryFromInventory(1,1));
    }

    @Test
    void deleteCategoryFromInventoryHavingProducts() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(categoryRepository.existsById(any())).thenReturn(true);
        when(productRepository.existsByCategoryId(any())).thenReturn(true);
        assertThrows(ValidationError.class, () -> employeeApiService.deleteCategoryFromInventory(1,1));
    }

    @Test
    void deleteCategoryFromInventory() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(categoryRepository.existsById(any())).thenReturn(true);
        when(productRepository.existsByCategoryId(any())).thenReturn(false);
        employeeApiService.deleteCategoryFromInventory(1,1);
    }

    @Test
    void createOrderWithInvalidUserRole() {
        mockUser.setRole("ADMIN");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        assertThrows(AuthenticationError.class, () -> employeeApiService.createOrder(1,1,1));
    }

    @Test
    void createOrderWithInvalidProductId() {
        mockUser.setRole("CUSTOMER");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(OrderCreationError.class, () -> employeeApiService.createOrder(1,1,1));
    }

    @Test
    void createOrderWithInvalidQuantity() {
        mockUser.setRole("CUSTOMER");
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(mockProduct));
        assertThrows(OrderCreationError.class, () -> employeeApiService.createOrder(1,1,0));
    }

    @Test
    void createOrderWithGreaterQuantityThanExisting() {
        mockUser.setRole("CUSTOMER");
        mockProduct.setQuantity(2);
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(mockProduct));
        assertThrows(OrderCreationError.class, () -> employeeApiService.createOrder(1,1,3));
    }

    @Test
    void createOrder() {
        mockUser.setRole("CUSTOMER");
        mockProduct.setQuantity(5);
        mockProduct.setPrice(20.0);
        Order order = new Order();
        order.setUserId(1);
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUser));
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(mockProduct));
        when(orderRepository.save(any())).thenReturn(order);
        when(productRepository.save(any())).thenReturn(mockProduct);
        OrderDto orderDto = employeeApiService.createOrder(1,1,3);
        assertThat(orderDto.getUserId()).isEqualTo(1);
    }
}
