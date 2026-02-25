package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemAnalytics;
import com.primebuild_online.repository.ItemAnalyticsRepository;
import com.primebuild_online.service.ItemAnalyticsService;
import com.primebuild_online.service.ItemService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemAnalyticsImpl implements ItemAnalyticsService {

    private final ItemAnalyticsRepository itemAnalyticsRepository;
    private final ItemService itemService;

    public ItemAnalyticsImpl(ItemAnalyticsRepository itemAnalyticsRepository,
                             @Lazy ItemService itemService) {
        this.itemAnalyticsRepository = itemAnalyticsRepository;
        this.itemService = itemService;
    }


    @Override
    public void saveItemAnalytics(Item item) {
        ItemAnalytics itemAnalytics = new ItemAnalytics();
        itemAnalytics.setTrendScore(0.0);
        itemAnalytics.setItem(item);
        itemAnalyticsRepository.save(itemAnalytics);
    }

    @Override
    public void atReduceItemQuantity(Item itemInDb, Integer quantityToReduce) {
        ItemAnalytics itemAnalytics = itemAnalyticsRepository.getByItem_Id(itemInDb.getId());
        BigDecimal currentRevenue = itemAnalytics.getTotalRevenue();
        itemAnalytics.setTotalSales(itemAnalytics.getTotalSales() + quantityToReduce);
        itemAnalytics.setTotalRevenue(
                currentRevenue
                        .add(itemService.calculateSubTotal(itemInDb, quantityToReduce)));
        itemAnalytics.setLastUpdatedAt(LocalDateTime.now());
        itemAnalytics.setTrendScore(calculateTrendScore(itemAnalytics));
        itemAnalyticsRepository.save(itemAnalytics);
    }

    @Override
    public void atResetItemQuantity(Item itemInDb, Integer quantityToAdd) {
        ItemAnalytics itemAnalytics = itemAnalyticsRepository.getByItem_Id(itemInDb.getId());
        BigDecimal currentRevenue = itemAnalytics.getTotalRevenue();
        itemAnalytics.setTotalSales(itemAnalytics.getTotalSales() - quantityToAdd);
        itemAnalytics.setTotalRevenue(
                currentRevenue
                        .subtract(itemService.calculateSubTotal(itemInDb, quantityToAdd)));
        itemAnalytics.setLastUpdatedAt(LocalDateTime.now());
        itemAnalytics.setTrendScore(calculateTrendScore(itemAnalytics));
        itemAnalyticsRepository.save(itemAnalytics);
    }

    @Override
    public boolean existsItemAnalyticsByItem(Long id) {
        return itemAnalyticsRepository.existsByItem_Id(id);
    }

    @Override
    public void atAddItemToCart(Item itemInDb, Integer quantity) {
        ItemAnalytics itemAnalytics = itemAnalyticsRepository.getByItem_Id(itemInDb.getId());
        itemAnalytics.setTotalCartAdds(
                itemAnalytics.getTotalCartAdds() + quantity
        );
        itemAnalytics.setLastUpdatedAt(LocalDateTime.now());
        itemAnalytics.setTrendScore(calculateTrendScore(itemAnalytics));
        itemAnalyticsRepository.save(itemAnalytics);
    }

    @Override
    public void atRemoveItemFromCart(Item itemInDb, Integer cartQuantity) {
        ItemAnalytics itemAnalytics = itemAnalyticsRepository.getByItem_Id(itemInDb.getId());
        itemAnalytics.setTotalCartAdds(
                itemAnalytics.getTotalCartAdds() - cartQuantity
        );
        itemAnalytics.setLastUpdatedAt(LocalDateTime.now());
        itemAnalytics.setTrendScore(calculateTrendScore(itemAnalytics));
        itemAnalyticsRepository.save(itemAnalytics);
    }

    @Override
    public void incrementView(Long id) {
        ItemAnalytics itemAnalytics = itemAnalyticsRepository.getByItem_Id(id);
        itemAnalytics.setTotalViews(itemAnalytics.getTotalViews() + 1);
        itemAnalytics.setLastUpdatedAt(LocalDateTime.now());
        itemAnalytics.setTrendScore(calculateTrendScore(itemAnalytics));
        itemAnalyticsRepository.save(itemAnalytics);
    }

    @Override
    public List<ItemAnalytics> getAllCartCounts() {
        return itemAnalyticsRepository.findAllByOrderByTotalCartAddsDesc();
    }

    @Override
    public List<ItemAnalytics> getAllSalesCounts() {
        return itemAnalyticsRepository.findAllByOrderByTotalSalesDesc();
    }

    @Override
    public List<ItemAnalytics> getAllViewCounts() {
        return itemAnalyticsRepository.findAllByOrderByTotalViewsDesc();
    }

    @Override
    public List<ItemAnalytics> getAllItemAnalyticsByTrendScore() {
        return itemAnalyticsRepository.findAllByOrderByTrendScoreDesc();
    }

    public double calculateTrendScore(ItemAnalytics analytics) {
        double viewsScore = analytics.getTotalViews() * 1.0;
        double cartScore = analytics.getTotalCartAdds() * 5.0;
        double salesScore = analytics.getTotalSales() * 10.0;

        double revenueScore = analytics.getTotalRevenue()
                .multiply(BigDecimal.valueOf(0.01))
                .doubleValue();
        return viewsScore + cartScore + salesScore + revenueScore;
    }
}
