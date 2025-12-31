package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Component component;

    @OneToMany(mappedBy = "item", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonIgnore
    private List<BuildItem> buildItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Manufacturer manufacturer;


}
