package com.largegroup.inventory_api.repository;

import com.largegroup.inventory_api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
