package com.largegroup.inventory_api.repository;

import com.largegroup.inventory_api.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    List<Category> findById(Integer categoryId, Pageable pageRequest);
    boolean existsByName(String name);
}
