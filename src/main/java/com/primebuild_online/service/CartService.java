package com.primebuild_online.service;

import com.primebuild_online.model.Cart;
import com.primebuild_online.model.DTO.CartDTO;

public interface CartService {
    Cart addItemsToCart(CartDTO cartDTO);

    Cart getCartByUser();

    void clearCart();
}
