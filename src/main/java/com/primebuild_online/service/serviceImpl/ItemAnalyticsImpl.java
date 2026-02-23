package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.repository.ItemAnalyticsRepository;
import com.primebuild_online.service.ItemAnalyticsService;
import com.primebuild_online.service.ItemService;
import org.springframework.stereotype.Service;

@Service
public class ItemAnalyticsImpl implements ItemAnalyticsService {

    private final ItemAnalyticsRepository itemAnalyticsRepository;
    private final ItemService itemService;

    public ItemAnalyticsImpl(ItemAnalyticsRepository itemAnalyticsRepository, ItemService itemService) {
        this.itemAnalyticsRepository = itemAnalyticsRepository;
        this.itemService = itemService;
    }


}
