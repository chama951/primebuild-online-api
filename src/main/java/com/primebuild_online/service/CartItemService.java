package com.primebuild_online.service;

import com.primebuild_online.model.Cart;
import com.primebuild_online.model.CartItem;
import com.primebuild_online.model.Item;

public interface CartItemService {
    CartItem saveCartItem(Item item, Cart cartInDb);

    void updateCartItemAtPriceChange(Long itemId);

    void deleteCartItemsByCartId(Long id);

    boolean existsCartItemByItem(Long id);
}
