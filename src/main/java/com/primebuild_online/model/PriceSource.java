package com.primebuild_online.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "price_source")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;     // Amazon, Newegg, LocalShop
    private String country;  // US, IN, LK
}
