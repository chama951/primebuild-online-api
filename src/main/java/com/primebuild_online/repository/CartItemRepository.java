package com.primebuild_online.repository;

import com.primebuild_online.model.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Transactional
    @Modifying
    void deleteAllByCart_Id(Long cartId);

    List<CartItem> findByItemId(Long itemId);

    List<CartItem> findAllByItem_Id(Long itemId);

    boolean existsByItem_Id(Long id);
}
