package com.primebuild_online.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "invoice_item")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

}
