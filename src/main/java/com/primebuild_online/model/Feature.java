package com.primebuild_online.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "feature")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_type_id")
    private Long featureTypeId;  // Links to FeatureType

    @Column(name = "feature_name")
    private String featureName;  // "LGA1700", "DDR5", "ATX", "8-pin"

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_type_id", insertable = false, updatable = false)
    private FeatureType featureType;

    @OneToMany(mappedBy = "feature")
    private List<ItemFeature> itemFeatures;
}
