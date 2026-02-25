package com.primebuild_online.controller;

import com.primebuild_online.model.ItemAnalytics;
import com.primebuild_online.service.ItemAnalyticsService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    ItemAnalyticsService itemAnalyticsService;

    @PostMapping("{id}")
    public void incrementView(@PathVariable Long id) {
        itemAnalyticsService.incrementView(id);
    }

    @GetMapping
    public List<ItemAnalytics> getAllItemAnalytics(@RequestParam(value = "attribute", required = false) String attribute) {
        if (attribute != null) {
            return switch (attribute.toLowerCase()) {
                case "carts" -> itemAnalyticsService.getAllCartCounts();
                case "sales" -> itemAnalyticsService.getAllSalesCounts();
                case "views" -> itemAnalyticsService.getAllViewCounts();
                default -> throw new PrimeBuildException(
                        "Unexpected value: " + attribute.toLowerCase(),
                        HttpStatus.BAD_REQUEST);
            };
        }
        return itemAnalyticsService.getAllItemAnalyticsByTrendScore();

    }
}
