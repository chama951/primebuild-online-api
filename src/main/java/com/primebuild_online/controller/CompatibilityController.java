package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.service.CompatibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/compatibility")
public class CompatibilityController {

    @Autowired
    private CompatibilityService compatibilityService;

    @PostMapping
    public Page<Item> getCompatibleItemByComponent(
            @RequestParam(value = "component", required = false) Long componentId,
            @RequestBody BuildReqDTO buildReqDTO,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return compatibilityService.getCompatibleItemsByComponent(buildReqDTO, componentId, pageable);
    }

    @PostMapping("/power_source")
    public List<Item> getCompatiblePowerSource(
            @RequestParam(value = "component", required = false) Long componentId,
            @RequestParam(value = "powerSource", required = false) Boolean powerSource,
            @RequestBody BuildReqDTO buildReqDTO){
            return compatibilityService.getCompatiblePowerSources(buildReqDTO, componentId, powerSource);
    }


}
