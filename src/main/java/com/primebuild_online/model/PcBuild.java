package com.primebuild_online.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@Table(name = "pc_build")
public class PcBuild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "build_name")
    private String buildName;

    @Column(name = "description")
    private String description;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "build_status")
    private String buildStatus; // "DRAFT", "COMPLETED", "COMPATIBLE", "INCOMPATIBLE"

    // Relationships
    @Column(name = "customer_id")
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "pc_build_items",
            joinColumns = @JoinColumn(name = "pc_build_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;

    // Constructors
    public PcBuild() {
        this.createdDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.isPublic = false;
        this.buildStatus = "DRAFT";
    }
}
