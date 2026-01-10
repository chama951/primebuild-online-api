package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.ManufacturerItem;
import com.primebuild_online.repository.ManufacturerItemRepository;
import com.primebuild_online.service.ManufacturerItemService;
import com.primebuild_online.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerItemServiceImpl implements ManufacturerItemService {

    @Autowired
    ManufacturerItemRepository manufacturerItemRepository;

    @Override
    public void saveManufacturerItem(ManufacturerItem manufacturerItem) {
        manufacturerItemRepository.save(manufacturerItem);
    }
}
