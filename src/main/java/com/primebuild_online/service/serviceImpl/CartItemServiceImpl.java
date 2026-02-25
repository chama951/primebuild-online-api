package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.*;
import com.primebuild_online.model.enumerations.NotificationType;
import com.primebuild_online.repository.CartItemRepository;
import com.primebuild_online.service.CartItemService;
import com.primebuild_online.service.ItemAnalyticsService;
import com.primebuild_online.service.ItemService;
import com.primebuild_online.service.NotificationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ItemService itemService;
    private final ItemAnalyticsService itemAnalyticsService;
    private final NotificationService notificationService;


    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               @Lazy ItemService itemService,
                               ItemAnalyticsService itemAnalyticsService,
                               NotificationService notificationService
    ) {
        this.cartItemRepository = cartItemRepository;
        this.itemService = itemService;
        this.itemAnalyticsService = itemAnalyticsService;
        this.notificationService = notificationService;
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
    public void updateCartItemAtPriceChange(List<CartItem> cartItemList) {
        for (CartItem cartItem : cartItemList) {
            if (cartItem.getUnitPrice().compareTo(cartItem.getItem().getPrice()) > 0) {
                notificationService.createNotification(
                        "Cart Item",
                        cartItem.getItem().getItemName() + " Price has been reduced",
                        NotificationType.CART_ITEM_PRICE_REDUCED,
                        cartItem.getCart().getUser());
            }
            cartItem.setUnitPrice(cartItem.getItem().getPrice());
            cartItem.setDiscountSubTotal(itemService.calculateDiscountSubTotal(cartItem.getItem(), cartItem.getCartQuantity()));
            cartItem.setDiscountPerUnite(itemService.calculateDiscountPerUnite(cartItem.getItem()));

            cartItem.setSubtotal(itemService.calculateSubTotal(cartItem.getItem(), cartItem.getCartQuantity()));
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
        for (CartItem cartItem : cartItemList) {
            Item item = cartItem.getItem();
            itemAnalyticsService.atRemoveItemFromCart(item, cartItem.getCartQuantity());
        }
    }

    @Override
    public BigDecimal calculateDiscountAmount(List<CartItem> cartItemList) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItemList) {
            discountAmount = discountAmount.add(cartItem.getDiscountSubTotal());
        }
        return discountAmount;
    }

    @Override
    public BigDecimal calculateTotalAmount(List<CartItem> cartItemList) {
        BigDecimal TotalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItemList) {
            TotalAmount = TotalAmount.add(cartItem.getSubtotal());
        }
        return TotalAmount;
    }
}
