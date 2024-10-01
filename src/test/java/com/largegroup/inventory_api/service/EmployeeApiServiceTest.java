package com.largegroup.inventory_api.service;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;
import java.util.List;

import com.largegroup.inventory_api.model.Category;
import com.largegroup.inventory_api.model.Product;
import com.largegroup.inventory_api.repository.CategoryRepository;
import com.largegroup.inventory_api.repository.ProductRepository;
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


    Product mockProduct;
    Category mockCategory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockProduct=new Product();
        mockCategory=new Category();
    
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

}
