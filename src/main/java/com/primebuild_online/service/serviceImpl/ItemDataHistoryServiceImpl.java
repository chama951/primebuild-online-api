package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemDataHistory;
import com.primebuild_online.model.enumerations.Vendors;
import com.primebuild_online.repository.ItemDataHistoryRepository;
import com.primebuild_online.service.ItemDataHistoryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ItemDataHistoryServiceImpl implements ItemDataHistoryService {
    private final ItemDataHistoryRepository itemDataHistoryRepository;

    public ItemDataHistoryServiceImpl(ItemDataHistoryRepository itemDataHistoryRepository) {

        this.itemDataHistoryRepository = itemDataHistoryRepository;
    }


    @Override
    public ItemDataHistory saveItemDataHistory(Item item,
                                               BigDecimal vendorPrice,
                                               Vendors vendor) {
        ItemDataHistory itemDataHistory = new ItemDataHistory();
        itemDataHistory.setItem(item);
        itemDataHistory.setVendor(vendor);
        itemDataHistory.setVendorPrice(vendorPrice);
        itemDataHistory.setRecordedAt(LocalDateTime.now());
        return itemDataHistoryRepository.save(itemDataHistory);
    }
}
