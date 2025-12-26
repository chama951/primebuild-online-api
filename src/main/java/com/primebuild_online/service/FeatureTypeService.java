package com.primebuild_online.service;

import com.primebuild_online.model.FeatureType;

import java.util.List;

public interface FeatureTypeService {

    FeatureType saveFeatureType(FeatureType featureType);

    List<FeatureType> getAllFeatureType();

    FeatureType getFeatureTypeById(long id);

    FeatureType updateFeatureType(FeatureType featureType, long id);

    void deleteFeatureType(long id);
}
