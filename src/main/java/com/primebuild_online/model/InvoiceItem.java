package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "invoice_item")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_quantity", nullable = false)
    private Integer invoiceQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "invoiceItems"})
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "invoiceItemList"})
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
