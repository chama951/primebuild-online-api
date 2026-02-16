package com.primebuild_online.service;

import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.ComponentFeatureTypeReqDTO;

import java.util.List;

public interface ComponentFeatureTypeService {
    ComponentFeatureType saveComponentFeatureTypeReq(ComponentFeatureTypeReqDTO componentFeatureTypeReqDTO);

    List<ComponentFeatureType> getAllComponentFeatureType();

    ComponentFeatureType updateComponentFeatureTypeReq(ComponentFeatureTypeReqDTO componentFeatureTypeReqDTO, Long id);

    ComponentFeatureType getComponentFeatureTypeById(Long id);

    void deleteComponentFeatureType(Long id);

    List<ComponentFeatureType> getComponentFeatureTypesByComponentId(Long componentId);

}
