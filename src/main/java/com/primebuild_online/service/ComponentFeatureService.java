package com.primebuild_online.service;

import com.primebuild_online.model.ComponentFeature;

import java.util.List;

public interface ComponentFeatureService {
    ComponentFeature saveComponentFeature(ComponentFeature componentFeature);

    List<ComponentFeature> getAllComponentFeature();

    ComponentFeature updateComponentFeature(ComponentFeature componentFeature, long id);

    ComponentFeature getComponentFeatureById(long id);

    void deleteComponentFeature(long id);
}
