package com.primebuild_online.service;

import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;

import java.util.List;

public interface CompatibilityService {
    List<Item> getCompatibleItemsByComponent(BuildReqDTO buildReqDTO, Long componentId);

    List<Item> getCompatiblePowerSources(BuildReqDTO buildReqDTO, Long componentId, Boolean powerSource);
}
