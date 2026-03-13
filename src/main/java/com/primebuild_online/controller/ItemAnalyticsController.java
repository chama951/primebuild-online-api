package com.primebuild_online.controller;

import com.primebuild_online.model.ItemAnalytics;
import com.primebuild_online.service.ItemAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/analytics")
public class ItemAnalyticsController {

    @Autowired
    ItemAnalyticsService itemAnalyticsService;

    @PostMapping("{id}")
    public void incrementView(@PathVariable Long id) {
        itemAnalyticsService.incrementView(id);
    }

    @GetMapping
    public Page<ItemAnalytics> getAllItemAnalytics(@RequestParam(value = "attribute", required = false) String attribute,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "8") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        if (Objects.equals(attribute, "carts")) {
            return itemAnalyticsService.getAllCartCounts(pageable);
        }
        if (Objects.equals(attribute, "sales")) {
            return itemAnalyticsService.getAllSalesCounts(pageable);
        }
        if (Objects.equals(attribute, "views")) {
            return itemAnalyticsService.getAllViewCounts(pageable);
        }
        return itemAnalyticsService.getAllItemAnalyticsByTrendScore(pageable);

    }
}
