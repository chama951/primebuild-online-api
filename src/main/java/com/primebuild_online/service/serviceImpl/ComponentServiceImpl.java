package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.ComponentReqDTO;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.ComponentRepository;
import com.primebuild_online.service.ComponentFeatureTypeService;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.FeatureTypeService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentServiceImpl implements ComponentService {
    private final ComponentRepository componentRepository;
    private final ComponentFeatureTypeService componentFeatureTypeService;
    private final FeatureTypeService featureTypeService;

    public ComponentServiceImpl(@Lazy ComponentFeatureTypeService componentFeatureTypeService,
                                ComponentRepository componentRepository,
                                @Lazy FeatureTypeService featureTypeService) {
        this.componentFeatureTypeService = componentFeatureTypeService;
        this.componentRepository = componentRepository;
        this.featureTypeService = featureTypeService;
    }

    @Override
    public Component saveComponentReq(ComponentReqDTO componentReqDTO) {
        Component newComponent = new Component();
        return componentRepository.save(setComponentValues(componentReqDTO, newComponent));
    }

    private Component setComponentValues(ComponentReqDTO componentReqDTO, Component component) {
        component.setComponentName(componentReqDTO.getComponentName());
        component.setBuildComponent(componentReqDTO.isBuildComponent());

        saveNewComponentFeatureTypes(componentReqDTO.getComponentFeatureTypeList(), component);
        return component;
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
    public Component getComponentById(Long id) {
        Optional<Component> component = componentRepository.findById(id);
        if (component.isPresent()) {
            return component.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public Component updateComponentReq(ComponentReqDTO componentReqDTO, Long id) {
        Component componentInDb = componentRepository.findById(id).orElseThrow(RuntimeException::new);
        componentInDb = setComponentValues(componentReqDTO, componentInDb);
        return componentRepository.save(componentInDb);
    }

    @Override
    public void deleteComponent(Long id) {
        componentRepository.findById(id).orElseThrow(RuntimeException::new);
        componentRepository.deleteById(id);
    }

    @Override
    public List<Component> getIsBuildComponentList() {
        return componentRepository.findAllByBuildComponent(true);
    }
}
