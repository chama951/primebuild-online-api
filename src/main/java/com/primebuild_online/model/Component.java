package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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
    private String componentName;  // "CPU", "Motherboard", "Memory", "GPU", etc.

    @OneToMany(mappedBy = "component")
    private List<Item> itemList;

    @OneToMany(mappedBy = "component")
    @JsonIgnore
    private List<ComponentFeatureType> componentFeatureTypeList;
}
