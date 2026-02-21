package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "item"})
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "invoiceList"})
//    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "added_at")
    private LocalDateTime addedAt = LocalDateTime.now();
}