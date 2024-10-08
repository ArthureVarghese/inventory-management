package com.largegroup.inventory_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Collections;

import com.largegroup.inventory_api.exception.GlobalExceptionHandler;
import com.largegroup.inventory_api.exception.ValidationError;
import com.largegroup.inventory_api.model.Category;
import com.largegroup.inventory_api.model.Product;
import com.largegroup.inventory_api.service.InventoryApiService;
import com.largegroup.inventory_api.view.CategoryList;
import com.largegroup.inventory_api.view.GenericResponse;
import com.largegroup.inventory_api.view.OrderDto;
import com.largegroup.inventory_api.view.ProductList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
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

@WebMvcTest (InventoryApiController.class)
@ExtendWith (MockitoExtension.class)
@AutoConfigureMockMvc
@ImportAutoConfiguration (GlobalExceptionHandler.class)
public class InventoryApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    InventoryApiService inventoryApiService;


    @Test
    void testGetProduct_WithNoParams() throws Exception {
        Product product = new Product();
        ProductList productList = new ProductList(Collections.singletonList(product));
        when(inventoryApiService.getProductFromInventory(any(), any(), anyInt(), any())).thenReturn(productList);

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
        when(inventoryApiService.getProductFromInventory(any(Integer.class), any(Integer.class), any(Integer.class),any())).thenReturn(productList);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/product")
                        .queryParam("product-id", "1")
                        .queryParam("category-id", "1")
                        .queryParam("page", "1")
                        .queryParam("active","true")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(1)));
    }

    @Test
    void testGetCategory_WithParam() throws Exception {

        Category category = new Category();
        CategoryList categoryList = new CategoryList(Collections.singletonList(category));

        when(inventoryApiService.getCategoryFromInventory(any(Integer.class), any(Integer.class),any())).thenReturn(categoryList);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/category")
                        .queryParam("category-id", "1")
                        .queryParam("active","true")
                        .queryParam("page", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(1)));

    }

    @Test
    void testGetCategory_WithNoParam() throws Exception {

        Category category = new Category();
        CategoryList categoryList = new CategoryList(Collections.singletonList(category));

        when(inventoryApiService.getCategoryFromInventory(any(), any(Integer.class),any())).thenReturn(categoryList);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/category")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(1)));

    }

    @Test
    void testUpdateProduct_Valid() throws Exception {

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/product")
                        .queryParam("product-id", "1")
                        .queryParam("user-id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void testUpdateProduct_WithNoParam() {
        try {
            mvc.perform(MockMvcRequestBuilders.put("/api/v1/product")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testUpdateCategory_Valid() throws Exception {

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/category")
                        .queryParam("category-id", "1")
                        .queryParam("name", "A")
                        .queryParam("user-id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void testUpdateCategory_WithNoParam() throws Exception {

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/category")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void addProductWithUnsupportedInput() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unsupported Media Type Provided"));

    }

    @Test
    void addProductWithMalformedJson() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid JSON Provided"));

    }

    @Test
    void addProductWithMissingUserId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name" : "as",
                                  "price" : 10,
                                  "quantity" : 10
                                }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'user-id'"));
    }

    @Test
    void addProductWithMissingJsonFields() throws Exception {
        when(inventoryApiService.addProductToInventory(any(), any())).thenThrow(ValidationError.class);
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .queryParam("user-id", "10")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name" : "as",
                                  "price" : 10,
                                  "quantity" : 10
                                }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addProduct() throws Exception {
        when(inventoryApiService.addProductToInventory(any(), any())).thenReturn(new GenericResponse(""));
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .queryParam("user-id", "9")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name" : "as",
                                  "price" : 10,
                                  "quantity" : 10
                                }"""))
                .andExpect(status().isCreated());
    }


    @Test
    void addCategoryWithUnsupportedInput() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/category")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unsupported Media Type Provided"));

    }

    @Test
    void addCategoryWithNoUserId() throws Exception {
        when(inventoryApiService.addProductToInventory(any(), any())).thenThrow(ValidationError.class);
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/category")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'user-id'"));

    }

    @Test
    void addCategoryWithMissingJsonFields() throws Exception {
        when(inventoryApiService.addCategoryToInventory(any(), any())).thenThrow(ValidationError.class);
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/category")
                        .queryParam("user-id", "10")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addCategory() throws Throwable{
        when(inventoryApiService.addCategoryToInventory(any(),any())).thenReturn(new GenericResponse(""));

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
                        .queryParam("user-id", "10")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                }"""))
                .andExpect(status().isCreated());
    }


    @Test
    void createOrderMissingUserId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/order")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'user-id'"));
    }

    @Test
    void createOrderMissingProductId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/order")
                        .queryParam("user-id","9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'product-id'"));
    }

    @Test
    void createOrderMissingQuantity() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/order")
                        .queryParam("user-id","9")
                        .queryParam("product-id","9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'quantity'"));
    }

    @Test
    void createOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setInvoiceId(1);
        when(inventoryApiService.createOrder(any(),any(),any())).thenReturn(orderDto);
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/order")
                        .queryParam("user-id","9")
                        .queryParam("product-id","9")
                        .queryParam("quantity","10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invoice_number").value("1"));
    }

}
