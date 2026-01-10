package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "component_feature_type")
public class ComponentFeatureType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer" , "handler", "itemList"})
    private Component component;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_type_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer" , "handler"})
    private FeatureType featureType;
}
