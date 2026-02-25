package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "build_item")
public class BuildItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "build_quantity")
    private Integer buildQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_id")
    @JsonIgnore
    private Build build;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Item item;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "discount_per_unit")
    private BigDecimal discountPerUnite;

    @Column(name = "discount_sub_total")
    private BigDecimal discountSubTotal;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

}
