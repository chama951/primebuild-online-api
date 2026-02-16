package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.ComponentFeatureTypeReqDTO;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.ComponentFeatureTypeRepository;
import com.primebuild_online.service.ComponentFeatureTypeService;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.FeatureTypeService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.utils.validator.ComponentFeatureTypeValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentFeatureTypeServiceImpl implements ComponentFeatureTypeService {
    private final ComponentFeatureTypeRepository componentFeatureTypeRepository;
    private final ComponentService componentService;
    private final FeatureTypeService featureTypeService;
    private final ComponentFeatureTypeValidator componentFeatureTypeValidator;

    public ComponentFeatureTypeServiceImpl(ComponentFeatureTypeRepository componentFeatureTypeRepository,
                                           ComponentService componentService,
                                           FeatureTypeService featureTypeService, ComponentFeatureTypeValidator componentFeatureTypeValidator) {
        this.componentFeatureTypeRepository = componentFeatureTypeRepository;
        this.componentService = componentService;
        this.featureTypeService = featureTypeService;
        this.componentFeatureTypeValidator = componentFeatureTypeValidator;
    }

    @Override
    public ComponentFeatureType saveComponentFeatureTypeReq(ComponentFeatureTypeReqDTO componentFeatureTypeReqDTO) {
        ComponentFeatureType newComponentFeatureType = new ComponentFeatureType();
        if (componentFeatureTypeReqDTO.getComponentId() != null) {
            Component component = componentService.getComponentById(componentFeatureTypeReqDTO.getComponentId());
            newComponentFeatureType.setComponent(component);
        }
        if (componentFeatureTypeReqDTO.getFeatureTypeId() != null) {
            FeatureType featureType = featureTypeService.getFeatureTypeById(componentFeatureTypeReqDTO.getFeatureTypeId());
            newComponentFeatureType.setFeatureType(featureType);
        }
        componentFeatureTypeValidator.validate(newComponentFeatureType);
        componentFeatureTypeRepository.save(newComponentFeatureType);
        return newComponentFeatureType;

    }

    @Override
    public List<ComponentFeatureType> getAllComponentFeatureType() {
        return componentFeatureTypeRepository.findAll();
    }

    @Override
    public ComponentFeatureType updateComponentFeatureTypeReq(ComponentFeatureTypeReqDTO componentFeatureTypeReqDTO, Long id) {
        ComponentFeatureType componentFeatureTypeInDb = componentFeatureTypeRepository.findById(id).orElseThrow(
                () -> new PrimeBuildException(
                        "Component Feature Type not found",
                        HttpStatus.NOT_FOUND));

        if (componentFeatureTypeReqDTO.getComponentId() != null) {
            Component component = componentService.getComponentById(componentFeatureTypeReqDTO.getComponentId());
            componentFeatureTypeInDb.setComponent(component);
        }
        if (componentFeatureTypeReqDTO.getFeatureTypeId() != null) {
            FeatureType featureType = featureTypeService.getFeatureTypeById(componentFeatureTypeReqDTO.getFeatureTypeId());
            componentFeatureTypeInDb.setFeatureType(featureType);
            componentFeatureTypeRepository.save(componentFeatureTypeInDb);
        }
        componentFeatureTypeValidator.validate(componentFeatureTypeInDb);
        return componentFeatureTypeInDb;
    }

    @Override
    public ComponentFeatureType getComponentFeatureTypeById(Long id) {
        Optional<ComponentFeatureType> componentFeature = componentFeatureTypeRepository.findById(id);
        if (componentFeature.isPresent()) {
            return componentFeature.get();
        } else {
            throw new PrimeBuildException(
                    "Component Feature Type not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteComponentFeatureType(Long id) {
        componentFeatureTypeRepository.deleteById(id);
    }

    @Override
    public List<ComponentFeatureType> getComponentFeatureTypesByComponentId(Long componentId) {
        return componentFeatureTypeRepository.getComponentFeatureTypesByComponentId(componentId);
    }
}
