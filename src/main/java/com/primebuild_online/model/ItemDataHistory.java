package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.primebuild_online.model.enumerations.Vendors;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item_data_history")
public class ItemDataHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor")
    private Vendors vendor;

    @Column(name = "vendor_price")
    private BigDecimal vendorPrice;

    @Column(name = "scraped_at", nullable = false)
    private LocalDateTime scrapedAt;
}
