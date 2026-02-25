package com.primebuild_online.service;

import com.primebuild_online.model.Cart;
import com.primebuild_online.model.CartItem;
import com.primebuild_online.model.Item;

import java.math.BigDecimal;
import java.util.List;

public interface CartItemService {
    CartItem saveCartItem(Item item, Cart cartInDb);


    void updateCartItemAtPriceChange(List<CartItem> cartItemList);

    void deleteCartAllItemsByCartId(Long id);

    boolean existsCartItemByItem(Long id);

    void removeCartItemList(List<CartItem> cartItemList);

    BigDecimal calculateDiscountAmount(List<CartItem> cartItemList);

    BigDecimal calculateTotalAmount(List<CartItem> cartItemList);
}
