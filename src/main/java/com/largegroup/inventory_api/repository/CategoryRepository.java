package com.largegroup.inventory_api.repository;

import java.util.List;

import com.largegroup.inventory_api.model.Category;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    List<Category> findById(Integer categoryId, Pageable pageRequest);
}
