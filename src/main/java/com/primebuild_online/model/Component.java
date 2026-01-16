package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "component")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "component_name", nullable = false)
    private String componentName;

    @Column(name = "is_build_component")
    private boolean isBuildComponent=true;

    @OneToMany(mappedBy = "component", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Item> itemList = new ArrayList<>();

    @OneToMany(mappedBy = "component", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "component", "featureList"})
    private List<ComponentFeatureType> componentFeatureTypeList = new ArrayList<>();

}
