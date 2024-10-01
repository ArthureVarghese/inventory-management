package com.largegroup.inventory_api.repository;

import java.util.List;

import com.largegroup.inventory_api.model.Product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findById(Integer productId, Pageable pageRequest);

    List<Product> findByCategoryId(Integer categoryId, Pageable pageRequest);

    List<Product> findByIdAndCategoryId(Integer productId, Integer categoryId, Pageable pageRequest);
}
