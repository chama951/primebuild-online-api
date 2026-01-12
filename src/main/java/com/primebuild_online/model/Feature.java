package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
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

    @Column(name = "feature_name")
    private String featureName;  // "LGA1700", "DDR5", "ATX", "8-pin"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_type_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private FeatureType featureType;

    @OneToMany(mappedBy = "feature", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<ItemFeature> itemFeatures = new ArrayList<>();
}
