package com.primebuild_online.service;

import com.primebuild_online.model.DTO.FeatureReqDTO;
import com.primebuild_online.model.Feature;

import java.util.List;

public interface FeatureService {
    Feature saveFeature(FeatureReqDTO featureReqDTO);

    List<Feature> getAllFeature();

    Feature getFeatureById(long id);

    Feature updateFeature(FeatureReqDTO featureReqDTO, long id);

    void deleteFeature(long id);
}
