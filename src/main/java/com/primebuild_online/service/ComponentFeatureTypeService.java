package com.primebuild_online.service;

import com.primebuild_online.model.ComponentFeatureType;

import java.util.List;

public interface ComponentFeatureTypeService {
    ComponentFeatureType saveComponentFeatureType(ComponentFeatureType componentFeatureType);

    List<ComponentFeatureType> getAllComponentFeatureType();

    ComponentFeatureType updateComponentFeatureType(ComponentFeatureType componentFeatureType, long id);

    ComponentFeatureType getComponentFeatureTypeById(long id);

    void deleteComponentFeatureType(long id);

    void deleteAllComponentFeatureTypeByComponentId(Long id);
}
