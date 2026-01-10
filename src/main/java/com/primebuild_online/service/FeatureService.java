package com.primebuild_online.service;

import com.primebuild_online.model.DTO.FeatureRequestDTO;
import com.primebuild_online.model.Feature;

import java.util.List;

public interface FeatureService {
    Feature saveFeature(FeatureRequestDTO featureRequestDTO);

    List<Feature> getAllFeature();

    Feature getFeatureById(long id);

    Feature updateFeature(FeatureRequestDTO featureRequestDTO, long id);

    void deleteFeature(long id);
}
