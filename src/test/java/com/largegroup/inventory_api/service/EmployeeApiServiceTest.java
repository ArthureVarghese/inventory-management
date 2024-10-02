package com.largegroup.inventory_api.service;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.largegroup.inventory_api.exception.AuthenticationError;
import com.largegroup.inventory_api.exception.ValidationError;
import com.largegroup.inventory_api.model.Category;
import com.largegroup.inventory_api.model.Product;
import com.largegroup.inventory_api.model.User;
import com.largegroup.inventory_api.repository.CategoryRepository;
import com.largegroup.inventory_api.repository.ProductRepository;
import com.largegroup.inventory_api.repository.UserRepository;
import com.largegroup.inventory_api.view.CategoryList;
import com.largegroup.inventory_api.view.ProductList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class EmployeeApiServiceTest {

    @InjectMocks
    private EmployeeApiService employeeApiService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;


    Product mockProduct;
    Category mockCategory;
    User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockProduct=new Product();
        mockCategory=new Category();
        mockUser=new User();
    
    }

    @Test
    void testGetProductFromInventory_WithAllParams(){

        List<Product> products=Collections.singletonList(mockProduct);
        when(productRepository.findByIdAndCategoryId(1,1,PageRequest.of(1, 25))).thenReturn(products);

        ProductList response=employeeApiService.getProductFromInventory(1, 1, 1);

        assertThat(response).isNotNull();
        assertThat(response.getProducts().size()).isEqualTo(1);  

    }

    @Test
    void testGetProductFromInventory_WithNoParams(){

        List<Product> products=Collections.singletonList(mockProduct);
        when(productRepository.findAll(PageRequest.of(0, 25))).thenReturn(new PageImpl<>(products));

        ProductList response=employeeApiService.getProductFromInventory(null, null, 0);

        assertThat(response).isNotNull();
        assertThat(response.getProducts().size()).isEqualTo(1);  

    }

    @Test
    void testGetProductFromInventory_WithOnlyCategoryId(){

        List<Product> products=Collections.singletonList(mockProduct);
        when(productRepository.findByCategoryId(1,PageRequest.of(0, 25))).thenReturn(products);

        ProductList response=employeeApiService.getProductFromInventory(null,1, 0);

        assertThat(response).isNotNull();
        assertThat(response.getProducts().size()).isEqualTo(1);  

    }

    @Test
    void testGetProductFromInventory_WithOnlyProductId(){

        List<Product> products=Collections.singletonList(mockProduct);
        when(productRepository.findById(1,PageRequest.of(0, 25))).thenReturn(products);

        ProductList response=employeeApiService.getProductFromInventory(1,null, 0);

        assertThat(response).isNotNull();
        assertThat(response.getProducts().size()).isEqualTo(1);  

    }

    @Test
    void testGetCategoryFromInventory_WithCategoryId() {

        when(categoryRepository.findById(1,PageRequest.of(0, 25))).thenReturn(Collections.singletonList(mockCategory));

        CategoryList response=employeeApiService.getCategoryFromInventory(1, 0);

        assertThat(response).isNotNull();
        assertThat(response.getCategories().size()).isEqualTo(1);  

    }

    @Test
    void testGetCategoryFromInventory_WithNoParam() {

        when(categoryRepository.findAll(PageRequest.of(0, 25))).thenReturn(new PageImpl<>(Collections.singletonList(mockCategory)));

        CategoryList response=employeeApiService.getCategoryFromInventory(null, 0);

        assertThat(response).isNotNull();
        assertThat(response.getCategories().size()).isEqualTo(1);  

    }

    @Test
    void testUpdateProductInInventory_ValidAllParam() {

        mockUser.setRole("Admin");

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(categoryRepository.existsById(1)).thenReturn(true);

        employeeApiService.updateProductInInventory(1,"Product",1,10.0,1,1);

        assertThat(mockProduct.getName()).isEqualTo("Product");
        assertThat(mockProduct.getCategoryId()).isEqualTo(1);
        assertThat(mockProduct.getPrice()).isEqualTo(10.0);
        assertThat(mockProduct.getQuantity()).isEqualTo(1);
        
    }

    @Test
    void testUpdateProductInInventory_NoParam() {

        ValidationError exception = assertThrows(ValidationError.class, () -> {
            employeeApiService.updateProductInInventory(1,null,null,null,null,1);
        });        
        assertThat(exception.getErrors().get(0)).isEqualTo("No parameters provided for updation");
        
    }

    @Test
    void testUpdateProductInInventory_InvalidUserId() {

        ValidationError exception = assertThrows(ValidationError.class, () -> {
            employeeApiService.updateProductInInventory(1,"Product",null,null,null,1);
        });        
        assertThat(exception.getErrors().get(0)).isEqualTo("Invalid User ID Found While Validating");
        
    }

    @Test
    void testUpdateProductInInventory_UserNotAnAdmin() {

        mockUser.setRole("Buyer");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        Exception exception = assertThrows(AuthenticationError.class, () -> {
            employeeApiService.updateProductInInventory(1,"Product",null,null,null,1);
        });        
        assertThat(exception.getMessage()).isEqualTo("Action is Not allowed For Current User Role");
        
    }

    @Test
    void testUpdateProductInInventory_InvalidProductId() {

        mockUser.setRole("Admin");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        ValidationError exception = assertThrows(ValidationError.class, () -> {
            employeeApiService.updateProductInInventory(1,"Product",null,null,null,1);
        });        
        assertThat(exception.getErrors().get(0)).isEqualTo("No Product Found with the Given Product-id");
        
    }

    @Test
    void testUpdateProductInInventory_SameProductName() {

        mockUser.setRole("Admin");
        mockProduct.setName("product");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        ValidationError exception = assertThrows(ValidationError.class, () -> {
            employeeApiService.updateProductInInventory(1,"Product",null,null,null,1);
        });        
        assertThat(exception.getErrors().get(0)).isEqualTo("Can't change to same product name");
        
    }

    @Test
    void testUpdateProductInInventory_InvalidCategoryId() {

        mockUser.setRole("Admin");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(categoryRepository.existsById(1)).thenReturn(false);

        ValidationError exception = assertThrows(ValidationError.class, () -> {
            employeeApiService.updateProductInInventory(1,"Product",1,null,null,1);
        });        
        assertThat(exception.getErrors().get(0)).isEqualTo("No Category Found with the Given Category-id");

        mockProduct.setCategoryId(1);
        exception = assertThrows(ValidationError.class, () -> {
            employeeApiService.updateProductInInventory(1,null,1,null,null,1);
        });        
        assertThat(exception.getErrors().get(0)).isEqualTo("Can't change to same category id");

    }

    @Test
    void testUpdateProductInInventory_InvalidPrice() {

        mockUser.setRole("Admin");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        ValidationError exception = assertThrows(ValidationError.class, () -> {
            employeeApiService.updateProductInInventory(1,null,null,0.0,null,1);
        });        
        assertThat(exception.getErrors().get(0)).isEqualTo("Price should be greater than 0");

        mockProduct.setPrice(10.0);
        exception = assertThrows(ValidationError.class, () -> {
            employeeApiService.updateProductInInventory(1,null,null,10.0,null,1);
        });        
        assertThat(exception.getErrors().get(0)).isEqualTo("Can't change to same price");

    }

    @Test
    void testUpdateProductInInventory_InvalidQuantity() {

        mockUser.setRole("Admin");
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));

        ValidationError exception = assertThrows(ValidationError.class, () -> {
            employeeApiService.updateProductInInventory(1,null,null,null,0,1);
        });        
        assertThat(exception.getErrors().get(0)).isEqualTo("Quantity should be greater than 0");

        mockProduct.setQuantity(45);
        exception = assertThrows(ValidationError.class, () -> {
            employeeApiService.updateProductInInventory(1,null,null,null,45,1);
        });        
        assertThat(exception.getErrors().get(0)).isEqualTo("Can't change to same quantity");

    }




}
