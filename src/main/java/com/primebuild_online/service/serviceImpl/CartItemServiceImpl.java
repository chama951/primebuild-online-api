package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Cart;
import com.primebuild_online.model.CartItem;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemAnalytics;
import com.primebuild_online.repository.CartItemRepository;
import com.primebuild_online.service.CartItemService;
import com.primebuild_online.service.ItemAnalyticsService;
import com.primebuild_online.service.ItemService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ItemService itemService;
    private final ItemAnalyticsService itemAnalyticsService;


    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               @Lazy ItemService itemService,
                               ItemAnalyticsService itemAnalyticsService) {
        this.cartItemRepository = cartItemRepository;
        this.itemService = itemService;
        this.itemAnalyticsService = itemAnalyticsService;
    }

    @Override
    public CartItem saveCartItem(Item itemToAdd, Cart cartInDb) {
        Item itemInDb = itemService.getItemById(itemToAdd.getId());

        itemService.checkItemsStockQuantity(itemInDb, itemToAdd);

        itemAnalyticsService.atAddItemToCart(itemInDb, itemToAdd.getQuantity());

        CartItem cartItem = new CartItem();
        cartItem.setItem(itemInDb);
        cartItem.setCart(cartInDb);
        cartItem.setCartQuantity(itemToAdd.getQuantity());
        cartItem.setUnitPrice(itemInDb.getPrice());
        cartItem.setDiscountSubTotal(itemService.calculateDiscountSubTotal(itemInDb, itemToAdd.getQuantity()));
        cartItem.setDiscountPerUnite(itemService.calculateDiscountPerUnite(itemInDb));
        cartItem.setSubtotal(itemService.calculateSubTotal(itemInDb, itemToAdd.getQuantity()));

        return cartItemRepository.save(cartItem);
    }

    @Override
    public void updateCartItemAtPriceChange(Long itemId) {
        Item itemInDb = itemService.getItemById(itemId);
        List<CartItem> cartItemList = cartItemRepository.findAllByItem_Id(itemId);
        for (CartItem cartItem : cartItemList) {
            cartItem.setUnitPrice(itemInDb.getPrice());
            cartItem.setDiscountSubTotal(itemService.calculateDiscountSubTotal(itemInDb, cartItem.getCartQuantity()));
            cartItem.setDiscountPerUnite(itemService.calculateDiscountPerUnite(itemInDb));
            cartItem.setSubtotal(itemService.calculateSubTotal(itemInDb, cartItem.getCartQuantity()));
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public void deleteCartAllItemsByCartId(Long id) {
        cartItemRepository.deleteAllByCart_Id(id);
    }

    @Override
    public boolean existsCartItemByItem(Long id) {
        return cartItemRepository.existsByItem_Id(id);
    }

    @Override
    public void removeCartItemList(List<CartItem> cartItemList) {
        for (CartItem cartItem : cartItemList){
            Item item = cartItem.getItem();
            itemAnalyticsService.atRemoveItemFromCart(item,cartItem.getCartQuantity());
        }
    }
}
