package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @Column(name = "feature_type_name")
    private String featureTypeName;

    @OneToMany(mappedBy = "featureType", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Feature> featureList = new ArrayList<>();

    @OneToMany(mappedBy = "featureType", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<ComponentFeatureType> componentFeatureTypes = new ArrayList<>();
}
