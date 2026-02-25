package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.*;
import com.primebuild_online.model.DTO.CartDTO;
import com.primebuild_online.repository.CartRepository;
import com.primebuild_online.security.SecurityUtils;
import com.primebuild_online.service.CartItemService;
import com.primebuild_online.service.CartService;
import com.primebuild_online.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final UserService userService;


    public CartServiceImpl(CartRepository cartRepository,
                           CartItemService cartItemService,
                           UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    private User loggedInUser() {
        return userService.getUserById(
                Objects.requireNonNull(SecurityUtils.getCurrentUser()).getId()
        );
    }

    public Cart saveUserCart() {
        Cart cart = new Cart();
        cart.setUser(loggedInUser());
        cart = cartRepository.save(cart);
        return cart;
    }

    @Override
    public Cart addItemsToCart(CartDTO cartDTO) {

        Cart cart = cartRepository.findByUser(loggedInUser())
                .orElseGet(this::saveUserCart);

        cartItemService.removeCartItemList(cart.getCartItemList());
        cart.getCartItemList().clear();

        if (cartDTO.getItemList() != null && !cartDTO.getItemList().isEmpty()) {
            createCartItems(cartDTO.getItemList(), cart);
        } else {
            //        cart empty, while the itemList is empty
            cart.setTotalAmount(BigDecimal.valueOf(0));
            cart.setDiscountAmount(BigDecimal.valueOf(0));
        }


        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUser() {
        return cartRepository.findByUser(loggedInUser())
                .orElseGet(this::saveUserCart);
    }

    @Override
    public void updateCartAtItemPriceChange(Item item) {
        List<Cart> cartList = cartRepository.findDistinctByCartItemList_Item(item);
        for (Cart cartInDb : cartList) {
            cartItemService.updateCartItemAtPriceChange(cartInDb.getCartItemList());
            cartInDb.setDiscountAmount(cartItemService.calculateDiscountAmount(cartInDb.getCartItemList()));
            cartInDb.setTotalAmount(cartItemService.calculateTotalAmount(cartInDb.getCartItemList()));
            cartRepository.save(cartInDb);
        }

    }

    private void createCartItems(List<Item> itemList, Cart cartInDb) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        CartItem cartItem;
        for (Item itemRequest : itemList) {
            cartItem = cartItemService.saveCartItem(itemRequest, cartInDb);
            totalAmount = totalAmount.add(cartItem.getSubtotal());
            discountAmount = discountAmount.add(cartItem.getDiscountSubTotal());
            cartInDb.getCartItemList().add(cartItem);
        }
        cartInDb.setDiscountAmount(discountAmount);
        cartInDb.setTotalAmount(totalAmount);
        cartRepository.save(cartInDb);
    }


}
