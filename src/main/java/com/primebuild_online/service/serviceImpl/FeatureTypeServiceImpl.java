package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.FeatureTypeReqDTO;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.FeatureTypeRepository;
import com.primebuild_online.service.ComponentFeatureTypeService;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.FeatureTypeService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeatureTypeServiceImpl implements FeatureTypeService {
    private final FeatureTypeRepository featureTypeRepository;
    private final ComponentFeatureTypeService componentFeatureTypeService;
    private final ComponentService componentService;

    public FeatureTypeServiceImpl(@Lazy ComponentFeatureTypeService componentFeatureTypeService,
                                  FeatureTypeRepository featureTypeRepository,
                                  ComponentService componentService) {
        this.componentFeatureTypeService = componentFeatureTypeService;
        this.featureTypeRepository = featureTypeRepository;
        this.componentService = componentService;
    }

    @Override
    public FeatureType saveFeatureTypeReq(FeatureTypeReqDTO featureTypeReqDTO) {
        FeatureType featureType = new FeatureType();
        featureType.setFeatureTypeName(featureTypeReqDTO.getFeatureTypeName());
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
            throw new RuntimeException();
        }
    }

    @Override
    public FeatureType updateFeatureTypeReq(FeatureTypeReqDTO featureTypeReqDTO, Long id) {

        FeatureType featureTypeInDb = featureTypeRepository.findById(id).orElseThrow(RuntimeException::new);
        featureTypeInDb.setFeatureTypeName(featureTypeReqDTO.getFeatureTypeName());

        featureTypeRepository.save(featureTypeInDb);
        return featureTypeInDb;
    }

    @Override
    public void deleteFeatureType(Long id) {
        featureTypeRepository.findById(id).orElseThrow(RuntimeException::new);
        featureTypeRepository.deleteById(id);
    }

    @Override
    public FeatureType saveFeatureTypeByComponent(FeatureTypeReqDTO featureTypeReqDTO) {
        FeatureType featureType = new FeatureType();
        featureType.setFeatureTypeName(featureTypeReqDTO.getFeatureTypeName());
        FeatureType savedFeatureType = featureTypeRepository.save(featureType);

        Component componentInDb = componentService.getComponentById(featureTypeReqDTO.getComponentId());
        ComponentFeatureType newComponentFeatureType = new ComponentFeatureType();
        newComponentFeatureType.setComponent(componentInDb);
        newComponentFeatureType.setFeatureType(savedFeatureType);
        componentFeatureTypeService.saveComponentFeatureType(newComponentFeatureType);

        return savedFeatureType;
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
