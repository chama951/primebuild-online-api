package com.primebuild_online.repository;

import com.primebuild_online.model.Cart;
import com.primebuild_online.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
