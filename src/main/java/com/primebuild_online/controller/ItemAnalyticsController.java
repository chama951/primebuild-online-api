package com.primebuild_online.controller;

import com.primebuild_online.model.ItemAnalytics;
import com.primebuild_online.service.ItemAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ItemAnalytics> getAllItemAnalytics(@RequestParam(value = "attribute", required = false) String attribute) {
        if (Objects.equals(attribute, "carts")) {
            return itemAnalyticsService.getAllCartCounts();
        }
        if (Objects.equals(attribute, "sales")) {
            return itemAnalyticsService.getAllSalesCounts();
        }
        if (Objects.equals(attribute, "views")) {
            return itemAnalyticsService.getAllViewCounts();
        }
        return itemAnalyticsService.getAllItemAnalyticsByTrendScore();

    }
}
