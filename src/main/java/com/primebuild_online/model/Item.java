package com.primebuild_online.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name")
    private String itemName;  // "Intel Core i7-13700K", "ASUS Z790", etc.

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "component_id")
    private Long componentId;      // Links to Component table

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", insertable = false, updatable = false)
    private Component component;

    @OneToMany(mappedBy = "item")
    private List<ItemFeature> itemFeatures;
}
