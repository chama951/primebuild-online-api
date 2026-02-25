package com.primebuild_online.controller;

import com.primebuild_online.model.Cart;
import com.primebuild_online.model.DTO.CartDTO;
import com.primebuild_online.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> addItemsToCart(@RequestBody CartDTO cartDTO) {
        return new ResponseEntity<>(cartService.addItemsToCart(cartDTO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Cart> getCurrentUserCart() {
        return new ResponseEntity<Cart>(cartService.getCartByUser(), HttpStatus.OK);
    }

}
