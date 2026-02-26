package com.primebuild_online.service;

import com.primebuild_online.model.Cart;
import com.primebuild_online.model.DTO.CartDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.User;

public interface CartService {
    Cart addItemsToCart(CartDTO cartDTO);

    Cart getCartByUser();

    void updateCartAtItemPriceChange(Item item);

    boolean checkUserCartIsEmpty(User user);
}
