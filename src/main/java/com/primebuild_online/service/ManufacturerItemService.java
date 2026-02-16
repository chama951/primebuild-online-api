package com.primebuild_online.service;

import com.primebuild_online.model.Item;
import com.primebuild_online.model.Manufacturer;
import com.primebuild_online.model.ManufacturerItem;

public interface ManufacturerItemService {
    void saveManufacturerItem(ManufacturerItem manufacturerItem);

    ManufacturerItem prepareManufacturerItem(Manufacturer manufacturer, Item item);
}
