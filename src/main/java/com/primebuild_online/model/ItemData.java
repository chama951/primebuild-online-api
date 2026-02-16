package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.primebuild_online.model.enumerations.Vendors;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scraped_product_data")
public class ItemData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor")
    private Vendors vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
//    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "itemFeatureList", "manufacturer", "component"})
    private Item item;

    @Column(name = "vendor_price")
    private BigDecimal vendorPrice;

    @Column(name = "our_price")
    private BigDecimal ourPrice;

    @Column(name = "discount_percentage")
    private BigDecimal discountPercentage;

    @Column(name = "scraped_at", nullable = false)
    private LocalDateTime scrapedAt;
}
