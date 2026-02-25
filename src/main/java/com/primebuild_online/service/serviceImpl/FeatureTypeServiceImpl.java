package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.FeatureTypeReqDTO;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.FeatureTypeRepository;
import com.primebuild_online.service.ComponentFeatureTypeService;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.FeatureTypeService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.utils.validator.FeatureTypeValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeatureTypeServiceImpl implements FeatureTypeService {
    private final FeatureTypeRepository featureTypeRepository;
    private final ComponentFeatureTypeService componentFeatureTypeService;
    private final FeatureTypeValidator featureTypeValidator;

    public FeatureTypeServiceImpl(@Lazy ComponentFeatureTypeService componentFeatureTypeService,
                                  FeatureTypeRepository featureTypeRepository, FeatureTypeValidator featureTypeValidator) {
        this.componentFeatureTypeService = componentFeatureTypeService;
        this.featureTypeRepository = featureTypeRepository;
        this.featureTypeValidator = featureTypeValidator;
    }

    @Override
    public FeatureType saveFeatureTypeReq(FeatureTypeReqDTO featureTypeReqDTO) {
        FeatureType featureType = new FeatureType();

        featureType.setFeatureTypeName(featureTypeReqDTO.getFeatureTypeName());

        if (featureTypeRepository.existsByFeatureTypeNameIgnoreCase(
                featureType.getFeatureTypeName())) {

            throw new PrimeBuildException(
                    "Feature type already exists",
                    HttpStatus.CONFLICT
            );
        }
        featureTypeValidator.validate(featureType);
        return featureTypeRepository.save(featureType);
    }

    @Override
    public List<FeatureType> getAllFeatureType() {
        return featureTypeRepository.findAll();
    }

    @Override
    public FeatureType getFeatureTypeById(Long id) {
        Optional<FeatureType> featureType = featureTypeRepository.findById(id);
        if (featureType.isPresent()) {
            return featureType.get();
        } else {
            throw new PrimeBuildException(
                    "Feature Type not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public FeatureType updateFeatureTypeReq(FeatureTypeReqDTO featureTypeReqDTO, Long id) {

        FeatureType featureTypeInDb = featureTypeRepository.findById(id).orElseThrow(
                () -> new PrimeBuildException(
                        "Feature Type not found",
                        HttpStatus.NOT_FOUND));

        featureTypeInDb.setFeatureTypeName(featureTypeReqDTO.getFeatureTypeName());

        if (featureTypeInDb.getId() != null &&
                featureTypeRepository.existsByFeatureTypeNameIgnoreCaseAndIdNot(
                        featureTypeInDb.getFeatureTypeName(),
                        featureTypeInDb.getId())) {
            throw new PrimeBuildException(
                    "Feature type already exists",
                    HttpStatus.CONFLICT
            );
        }

        featureTypeValidator.validate(featureTypeInDb);
        featureTypeRepository.save(featureTypeInDb);
        return featureTypeInDb;
    }

    @Override
    public void deleteFeatureType(Long id) {
        featureTypeRepository.deleteById(id);
    }

    @Override
    public List<FeatureType> getFeatureTypesByComponent(Long componentId) {
        List<FeatureType> featureTypeList = new ArrayList<>();
        if (componentId != null) {
            List<ComponentFeatureType> componentFeatureTypeList = componentFeatureTypeService.getComponentFeatureTypesByComponentId(componentId);
            for (ComponentFeatureType componentFeatureType : componentFeatureTypeList) {
                featureTypeList.add(componentFeatureType.getFeatureType());
            }
        }
        return featureTypeList;
    }

}
