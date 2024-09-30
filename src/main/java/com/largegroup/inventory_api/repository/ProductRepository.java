package com.largegroup.inventory_api.repository;

import com.largegroup.inventory_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
