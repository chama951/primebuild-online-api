package com.primebuild_online.service;

import com.primebuild_online.model.DTO.FeatureTypeReqDTO;
import com.primebuild_online.model.FeatureType;

import java.util.List;

public interface FeatureTypeService {

    FeatureType saveFeatureTypeReq(FeatureTypeReqDTO featureTypeReqDTO);

    List<FeatureType> getAllFeatureType();

    FeatureType getFeatureTypeById(Long id);

    FeatureType updateFeatureTypeReq(FeatureTypeReqDTO featureTypeReqDTO, Long id);

    void deleteFeatureType(Long id);

//    FeatureType saveFeatureTypeByComponent(FeatureTypeReqDTO featureTypeReqDTO);

    List<FeatureType> getFeatureTypesByComponent(Long componentId);
}
