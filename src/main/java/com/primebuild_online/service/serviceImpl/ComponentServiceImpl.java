package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.ComponentDTO;
import com.primebuild_online.model.Feature;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.ComponentRepository;
import com.primebuild_online.service.ComponentFeatureTypeService;
import com.primebuild_online.service.ComponentService;
import com.primebuild_online.service.FeatureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private ComponentRepository componentRepository;
    @Autowired
    private ComponentFeatureTypeService componentFeatureTypeService;
    @Autowired
    private FeatureTypeService featureTypeService;

    @Override
    public Component saveComponent(ComponentDTO componentDTO) {
        Component newComponent = new Component();
        newComponent = setComponentValues(componentDTO, newComponent);
        return componentRepository.save(newComponent);
    }

    private Component setComponentValues(ComponentDTO componentDTO, Component component) {
        component.setComponentName(componentDTO.getComponentName());
        Component savedComponent = componentRepository.save(component);
        saveNewComponentFeatureTypes(componentDTO.getFeatureTypeList(), savedComponent);
        return savedComponent;
    }

    private void saveNewComponentFeatureTypes(List<FeatureType> featureTypeList, Component component) {

        if (featureTypeList != null) {
            System.out.println("saveNewComponentFeatureTypes");
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
    public List<ComponentDTO> getAllComponent() {
        List<Component> componentList = componentRepository.findAll();
        List<ComponentDTO> componentDTOList = new ArrayList<>();

        for (Component component : componentList) {
            ComponentDTO componentDTO = new ComponentDTO();
            componentDTO.setId(component.getId());
            componentDTO.setComponentName(component.getComponentName());
            componentDTOList.add(componentDTO);
        }

        return componentDTOList;
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
    public Component updateComponentRequest(ComponentDTO componentDTO, long id) {
        Component componentInDb = componentRepository.findById(id).orElseThrow(RuntimeException::new);
        componentInDb = setComponentValues(componentDTO, componentInDb);
        return componentRepository.save(componentInDb);
    }

    @Override
    public void deleteComponent(long id) {
        componentRepository.findById(id).orElseThrow(RuntimeException::new);
        componentRepository.deleteById(id);
    }
}
