package com.largegroup.inventory_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Collections;

import com.largegroup.inventory_api.model.Category;
import com.largegroup.inventory_api.model.Product;
import com.largegroup.inventory_api.service.EmployeeApiService;
import com.largegroup.inventory_api.view.CategoryList;
import com.largegroup.inventory_api.view.ProductList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest (EmployeeApiController.class)
@ExtendWith (MockitoExtension.class)
@AutoConfigureMockMvc
public class EmployeeApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    EmployeeApiService employeeApiService;
    
    @Test
    void testGetProduct_WithNoParams() throws Exception {
        Product product = new Product();
        ProductList productList = new ProductList(Collections.singletonList(product));
        when(employeeApiService.getProductFromInventory(any(), any(), anyInt())).thenReturn(productList);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/product")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(1)));
}

    @Test
    void testGetProduct_WithWrongPagination() {

        try {
            mvc.perform(MockMvcRequestBuilders.get("/api/v1/product")
                    .queryParam("page", "-1")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Invalid Page Number Provided"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void testGetProduct_WithAllParams() throws Exception {
        Product product = new Product();
        ProductList productList = new ProductList(Collections.singletonList(product));
        when(employeeApiService.getProductFromInventory(any(Integer.class),any(Integer.class),any(Integer.class))).thenReturn(productList);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/product")
                .queryParam("product-id", "1")
                .queryParam("category-id", "1")
                .queryParam("page", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(1)));
    }

    @Test
    void testGetCategory_WithParam() throws Exception {

        Category category=new Category();
        CategoryList categoryList=new CategoryList(Collections.singletonList(category));

        when(employeeApiService.getCategoryFromInventory(any(Integer.class),any(Integer.class))).thenReturn(categoryList);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/category")
                .queryParam("category-id", "1")
                .queryParam("page", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(1)));
        
    }

    @Test
    void testGetCategory_WithNoParam() throws Exception {

        Category category=new Category();
        CategoryList categoryList=new CategoryList(Collections.singletonList(category));

        when(employeeApiService.getCategoryFromInventory(any(),any(Integer.class))).thenReturn(categoryList);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/category")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(1)));
        
    }

    @Test
    void testUpdateProduct_Valid() throws Exception{

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/product")
                .queryParam("product-id","1")
                .queryParam("user-id","1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
    }

    @Test
    void testUpdateProduct_WithNoParam() throws Exception{
        try{
            mvc.perform(MockMvcRequestBuilders.put("/api/v1/product")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testUpdateCategory_Valid() throws Exception {

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/category")
                .queryParam("category-id","1")
                .queryParam("name","A")
                .queryParam("user-id","1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
    }

    @Test
    void testUpdateCategory_WithNoParam() throws Exception {

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/category")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        
    }

}
