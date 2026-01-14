package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.ComponentReqDTO;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.ComponentRepository;
import com.primebuild_online.service.ComponentFeatureTypeService;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.FeatureTypeService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private ComponentRepository componentRepository;

    @Getter
    private ComponentFeatureTypeService componentFeatureTypeService;

    @Getter
    private FeatureTypeService featureTypeService;

    @Autowired
    public void setFeatureTypeService(@Lazy FeatureTypeService featureTypeService) {
        this.featureTypeService = featureTypeService;
    }

    @Autowired
    public void setComponentFeatureTypeService(@Lazy ComponentFeatureTypeService componentFeatureTypeService) {
        this.componentFeatureTypeService = componentFeatureTypeService;
    }


    @Override
    public Component saveComponentReq(ComponentReqDTO componentReqDTO) {
        Component newComponent = new Component();
        newComponent = setComponentValues(componentReqDTO, newComponent);
        return componentRepository.save(newComponent);
    }

    private Component setComponentValues(ComponentReqDTO componentReqDTO, Component component) {
        component.setComponentName(componentReqDTO.getComponentName());
        Component savedComponent = componentRepository.save(component);
        saveNewComponentFeatureTypes(componentReqDTO.getFeatureTypeList(), savedComponent);
        return savedComponent;
    }

    private void saveNewComponentFeatureTypes(List<FeatureType> featureTypeList, Component component) {

        if (featureTypeList != null) {
            componentFeatureTypeService.deleteAllComponentFeatureTypeByComponentId(component.getId());
            for (FeatureType featureTypeRequest : featureTypeList) {
                FeatureType featureType = featureTypeService.getFeatureTypeById(featureTypeRequest.getId());
                ComponentFeatureType componentFeatureType = new ComponentFeatureType();
                componentFeatureType.setComponent(component);
                componentFeatureType.setFeatureType(featureType);
                componentFeatureTypeService.saveComponentFeatureType(componentFeatureType);
            }
        }

    }

    @Override
    public List<Component> getAllComponent() {
        return componentRepository.findAll();
    }

    @Override
    public Component getComponentById(long id) {
        Optional<Component> component = componentRepository.findById(id);
        if (component.isPresent()) {
            return component.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public Component updateComponentReq(ComponentReqDTO componentReqDTO, long id) {
        Component componentInDb = componentRepository.findById(id).orElseThrow(RuntimeException::new);
        componentInDb = setComponentValues(componentReqDTO, componentInDb);
        return componentRepository.save(componentInDb);
    }

    @Override
    public void deleteComponent(long id) {
        componentRepository.findById(id).orElseThrow(RuntimeException::new);
        componentRepository.deleteById(id);
    }
}
