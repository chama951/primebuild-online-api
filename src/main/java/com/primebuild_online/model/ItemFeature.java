package com.primebuild_online.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "item_feature")
public class ItemFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;      // Links to Item

    @Column(name = "feature_id")
    private Long featureId;   // Links to Feature

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", insertable = false, updatable = false)
    private Feature feature;
}
