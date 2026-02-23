package com.primebuild_online.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "item_analytics")
public class ItemAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "itemAnalytics"})
    private Item item;

    @Column(name = "total_views")
    private Long totalViews = 0L;

    @Column(name = "total_sales")
    private Long totalSales = 0L;

    @Column(name = "total_cart_adds")
    private Long totalCartAdds = 0L;

    @Column(name = "total_revenue")
    private Double totalRevenue = 0.0;

    @Column(name = "trend_score")
    private Double trendScore = 0.0;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

}
