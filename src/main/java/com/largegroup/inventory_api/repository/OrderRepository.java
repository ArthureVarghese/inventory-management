package com.largegroup.inventory_api.repository;

import com.largegroup.inventory_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Integer, Order> {
}
