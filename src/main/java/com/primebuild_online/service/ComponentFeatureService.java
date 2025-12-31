package com.primebuild_online.service;

import com.primebuild_online.model.ComponentFeatureType;

import java.util.List;

public interface ComponentFeatureService {
    ComponentFeatureType saveComponentFeature(ComponentFeatureType componentFeatureType);

    List<ComponentFeatureType> getAllComponentFeature();

    ComponentFeatureType updateComponentFeature(ComponentFeatureType componentFeatureType, long id);

    ComponentFeatureType getComponentFeatureById(long id);

    void deleteComponentFeature(long id);
}
