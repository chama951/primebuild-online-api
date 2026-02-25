package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.primebuild_online.model.enumerations.Vendors;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item_data")
public class ItemData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "itemDataHistoryList"})
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor")
    private Vendors vendor;

    @Column(name = "vendor_price")
    private BigDecimal vendorPrice;

    @Column(name = "our_price")
    private BigDecimal ourPrice;

    @Column(name = "discount_percentage")
    private BigDecimal discountPercentage;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
}
