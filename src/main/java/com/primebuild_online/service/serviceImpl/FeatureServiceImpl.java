package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.FeatureReqDTO;
import com.primebuild_online.model.Feature;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.FeatureRepository;
import com.primebuild_online.service.FeatureService;
import com.primebuild_online.service.FeatureTypeService;
import com.primebuild_online.service.ItemFeatureService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.utils.validator.FeatureValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureServiceImpl implements FeatureService {
    private final FeatureRepository featureRepository;
    private final FeatureTypeService featureTypeService;
    private final FeatureValidator featureValidator;


    public FeatureServiceImpl(FeatureRepository featureRepository,
                              FeatureTypeService featureTypeService, FeatureValidator featureValidator) {
        this.featureRepository = featureRepository;
        this.featureTypeService = featureTypeService;
        this.featureValidator = featureValidator;
    }

    @Override
    public Feature saveFeatureReq(FeatureReqDTO featureReqDTO) {
        Feature feature = new Feature();

        feature = setValues(feature, featureReqDTO);

        return featureRepository.save(feature);
    }

    private Feature setValues(Feature feature, FeatureReqDTO featureReqDTO) {

        feature.setFeatureName(featureReqDTO.getFeatureName());

        if (featureReqDTO.getFeatureTypeId() != null) {
            FeatureType featureType = featureTypeService.getFeatureTypeById(featureReqDTO.getFeatureTypeId());
            feature.setFeatureType(featureType);
        }

        if (feature.getId() != null &&
                featureRepository.existsByFeatureNameIgnoreCaseAndFeatureTypeId(
                        feature.getFeatureName(),
                        feature.getFeatureType().getId())) {

            throw new PrimeBuildException(
                    "Feature already exists for this feature type",
                    HttpStatus.CONFLICT
            );
        }
        featureValidator.validate(feature);
        return feature;
    }


    @Override
    public List<Feature> getAllFeature() {
        return featureRepository.findAll();
    }

    @Override
    public Feature getFeatureById(Long id) {
        Optional<Feature> feature = featureRepository.findById(id);
        if (feature.isPresent()) {
            return feature.get();
        } else {
            throw new PrimeBuildException(
                    "Feature not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Feature updateFeatureReq(FeatureReqDTO featureReqDTO, Long id) {

        Feature featureInDb = featureRepository.findById(id).orElseThrow(
                () -> new PrimeBuildException(
                        "Feature not found",
                        HttpStatus.NOT_FOUND));

        featureInDb = setValues(featureInDb, featureReqDTO);

        featureRepository.save(featureInDb);
        return featureInDb;
    }

    @Override
    public void deleteFeature(Long id) {
        featureRepository.deleteById(id);
    }

    @Override
    public List<Feature> getFeatureListByFeatureType(Long featureTypeId) {
        return featureRepository.findAllByFeatureTypeId(featureTypeId);
    }

}
