package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Item;
import com.primebuild_online.model.enumerations.NotificationType;
import com.primebuild_online.service.InventoryService;
import com.primebuild_online.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final NotificationService notificationService;

    public InventoryServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void checkLowStock(Item item) {
        if (item.getQuantity() <= item.getLowStockThreshold()) {
            notificationService.createNotification(
                    "Low Stock Alert",
                    item.getItemName() + " is running low",
                    NotificationType.LOW_STOCK
            );
        }
    }
}
