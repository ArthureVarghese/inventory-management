package com.largegroup.inventory_api.repository;

import com.largegroup.inventory_api.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findById(Integer productId, Pageable pageRequest);

    List<Product> findByCategoryId(Integer categoryId, Pageable pageRequest);

    List<Product> findByIdAndCategoryId(Integer productId, Integer categoryId, Pageable pageRequest);

    boolean existsByNameAndCategoryId(String name, Integer categoryId);

    boolean existsByCategoryId(Integer categoryId);

    boolean existsByCategoryIdAndActiveIsTrue(Integer categoryId);
}
