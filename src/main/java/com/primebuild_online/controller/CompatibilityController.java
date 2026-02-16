package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.service.CompatibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compatibility")
public class CompatibilityController {

    @Autowired
    private CompatibilityService compatibilityService;

    @PostMapping
    public List<Item> getCompatibleItemByComponent(
            @RequestParam(value = "component", required = false) Long componentId,
            @RequestBody BuildReqDTO buildReqDTO) {
        return compatibilityService.getCompatibleItemsByComponent(buildReqDTO, componentId);
    }

    @PostMapping("/power_source")
    public List<Item> getCompatiblePowerSource(
            @RequestParam(value = "component", required = false) Long componentId,
            @RequestParam(value = "powerSource", required = false) Boolean powerSource,
            @RequestBody BuildReqDTO buildReqDTO){
            return compatibilityService.getCompatiblePowerSources(buildReqDTO, componentId, powerSource);
    }


}
