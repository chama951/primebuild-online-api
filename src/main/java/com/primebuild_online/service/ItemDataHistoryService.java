package com.primebuild_online.service;

import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemDataHistory;
import com.primebuild_online.model.enumerations.Vendors;

import java.math.BigDecimal;

public interface ItemDataHistoryService {
    ItemDataHistory saveItemDataHistory(Item item,
                                        BigDecimal vendorPrice,
                                        Vendors vendor);
}
