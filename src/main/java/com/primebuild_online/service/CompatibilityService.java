package com.primebuild_online.service;

import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompatibilityService {
    Page<Item> getCompatibleItemsByComponent(BuildReqDTO buildReqDTO, Long componentId,Pageable pageable);

    List<Item> getCompatiblePowerSources(BuildReqDTO buildReqDTO, Long componentId, Boolean powerSource);
}
