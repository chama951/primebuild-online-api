package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.ManufacturerItem;
import com.primebuild_online.repository.ManufacturerItemRepository;
import com.primebuild_online.service.ManufacturerItemService;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerItemServiceImpl implements ManufacturerItemService {
    private final ManufacturerItemRepository manufacturerItemRepository;

    public ManufacturerItemServiceImpl(ManufacturerItemRepository manufacturerItemRepository) {
        this.manufacturerItemRepository = manufacturerItemRepository;
    }

    @Override
    public void saveManufacturerItem(ManufacturerItem manufacturerItem) {
        manufacturerItemRepository.save(manufacturerItem);
    }
}
