package com.primebuild_online.service;

import com.primebuild_online.model.DTO.FeatureReqDTO;
import com.primebuild_online.model.Feature;

import java.util.List;

public interface FeatureService {
    Feature saveFeatureReq(FeatureReqDTO featureReqDTO);

    List<Feature> getAllFeature();

    Feature getFeatureById(Long id);

    Feature updateFeatureReq(FeatureReqDTO featureReqDTO, Long id);

    void deleteFeature(Long id);

    List<Feature> getFeatureListByFeatureType(Long featureTypeId);
}
