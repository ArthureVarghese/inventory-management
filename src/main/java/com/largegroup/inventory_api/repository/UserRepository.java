package com.largegroup.inventory_api.repository;

import com.largegroup.inventory_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Integer, User> {
}
