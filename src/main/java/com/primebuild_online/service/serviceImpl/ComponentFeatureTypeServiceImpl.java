package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.ComponentFeatureTypeReqDTO;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.ComponentFeatureTypeRepository;
import com.primebuild_online.service.ComponentFeatureTypeService;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.FeatureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentFeatureTypeServiceImpl implements ComponentFeatureTypeService {

    @Autowired
    private ComponentFeatureTypeRepository componentFeatureTypeRepository;

    private ComponentService componentService;

    private FeatureTypeService featureTypeService;

    @Autowired
    public void setComponentService(@Lazy ComponentService componentService) {
        this.componentService = componentService;
    }

    @Autowired
    public void setFeatureTypeService(@Lazy FeatureTypeService featureTypeService) {
        this.featureTypeService = featureTypeService;
    }

    @Override
    public ComponentFeatureType saveComponentFeatureType(ComponentFeatureType componentFeatureType) {
        componentFeatureType.setComponent(componentFeatureType.getComponent());
        componentFeatureType.setFeatureType(componentFeatureType.getFeatureType());
        return componentFeatureTypeRepository.save(componentFeatureType);
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
        componentFeatureTypeRepository.save(newComponentFeatureType);
        return newComponentFeatureType;

    }

    @Override
    public List<ComponentFeatureType> getAllComponentFeatureType() {
        return componentFeatureTypeRepository.findAll();
    }

    @Override
    public ComponentFeatureType updateComponentFeatureType(ComponentFeatureTypeReqDTO componentFeatureTypeReqDTO, long id) {
        ComponentFeatureType componentFeatureTypeInDb = componentFeatureTypeRepository.findById(id).orElseThrow(RuntimeException::new);
        if (componentFeatureTypeReqDTO.getComponentId() != null) {
            Component component = componentService.getComponentById(componentFeatureTypeReqDTO.getComponentId());
            componentFeatureTypeInDb.setComponent(component);
            componentFeatureTypeRepository.save(componentFeatureTypeInDb);
        } else {
            componentFeatureTypeRepository.delete(componentFeatureTypeInDb);
        }
        if (componentFeatureTypeReqDTO.getFeatureTypeId() != null) {
            FeatureType featureType = featureTypeService.getFeatureTypeById(componentFeatureTypeReqDTO.getFeatureTypeId());
            componentFeatureTypeInDb.setFeatureType(featureType);
            componentFeatureTypeRepository.save(componentFeatureTypeInDb);
        } else {
            componentFeatureTypeRepository.delete(componentFeatureTypeInDb);
        }

        return componentFeatureTypeInDb;
    }

    @Override
    public ComponentFeatureType getComponentFeatureTypeById(long id) {
        Optional<ComponentFeatureType> componentFeature = componentFeatureTypeRepository.findById(id);
        if (componentFeature.isPresent()) {
            return componentFeature.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteComponentFeatureType(long id) {
        componentFeatureTypeRepository.findById(id).orElseThrow(RuntimeException::new);
        componentFeatureTypeRepository.deleteById(id);
    }

    @Override
    public void deleteAllComponentFeatureTypeByComponentId(Long id) {
        componentFeatureTypeRepository.deleteAllComponentFeatureTypeByComponentId(id);
    }

    @Override
    public List<ComponentFeatureType> getComponentFeatureTypesByComponentId(Long componentId) {
        return componentFeatureTypeRepository.getComponentFeatureTypesByComponentId(componentId);
    }
}
