package com.primebuild_online.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "feature_type")
public class FeatureType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_name")
    private String featureName;  // "Socket", "Memory Type", "Form Factor", "Power Connector"

    // Relationships
    @OneToMany(mappedBy = "featureType")
    private List<Feature> features;

    @OneToMany(mappedBy = "featureType")
    private List<ComponentFeature> componentFeatures;
}
