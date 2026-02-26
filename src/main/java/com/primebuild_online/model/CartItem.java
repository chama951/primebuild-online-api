package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "item"})
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "cartItemList"})
    private Cart cart;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "discount_per_unit")
    private BigDecimal discountPerUnite;

    @Column(name = "discount_sub_total")
    private BigDecimal discountSubTotal;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "cart_quantity", nullable = false)
    private Integer cartQuantity;

    @Column(name = "added_at")
    private LocalDateTime addedAt = LocalDateTime.now();
}