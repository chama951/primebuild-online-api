package com.primebuild_online.service;

import com.primebuild_online.model.Feature;

import java.util.List;

public interface FeatureService {
    Feature saveFeature(Feature feature);

    List<Feature> getAllFeature();

    Feature getFeatureById(long id);

    Feature updateFeature(Feature feature, long id);

    void deleteFeature(long id);
}
