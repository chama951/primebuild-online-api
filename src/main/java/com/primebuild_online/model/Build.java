package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "build")
public class Build {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "build_status")
    private String buildStatus; // "DRAFT", "COMPLETED", "COMPATIBLE", "INCOMPATIBLE"
//
//    @Column(name = "customer_id")
//    private Long customerId;

    @OneToMany(mappedBy = "build", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<BuildItem> buildItems;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
}
