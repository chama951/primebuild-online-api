package com.primebuild_online.repository;

import com.primebuild_online.model.Cart;
import com.primebuild_online.model.CartItem;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    List<Cart> findDistinctByCartItemList_Item(Item item);
}
