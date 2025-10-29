package com.primebuild_online.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "component_feature")
public class ComponentFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "component_id")
    private Long componentId;      // Links to Component

    @Column(name = "feature_type_id")
    private Long featureTypeId;    // Links to FeatureType

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", insertable = false, updatable = false)
    private Component component;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_type_id", insertable = false, updatable = false)
    private FeatureType featureType;
}
