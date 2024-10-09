package com.largegroup.inventory_api.repository;

import com.largegroup.inventory_api.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsByNameAndCategoryId(String name, Integer categoryId);

    boolean existsByCategoryId(Integer categoryId);

    boolean existsByCategoryIdAndActiveIsTrue(Integer categoryId);

    List<Product> findByCategoryIdAndActive(Integer categoryId, Boolean active, Pageable pageRequest);

    List<Product> findByActive(Boolean active, Pageable pageRequest);

    List<Product> findByIdAndCategoryIdAndActive(Integer productId, Integer categoryId, Boolean active, Pageable pageRequest);

    List<Product> findByIdAndActive(Integer productId, Boolean active, Pageable pageRequest);
}
