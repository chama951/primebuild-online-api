package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.repository.ComponentFeatureTypeRepository;
import com.primebuild_online.service.ComponentFeatureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentFeatureTypeServiceImpl implements ComponentFeatureTypeService {

    @Autowired
    private ComponentFeatureTypeRepository componentFeatureTypeRepository;

    @Override
    public ComponentFeatureType saveComponentFeatureType(ComponentFeatureType componentFeatureType){
        componentFeatureType.setComponent(componentFeatureType.getComponent());
        componentFeatureType.setFeatureType(componentFeatureType.getFeatureType());
        return componentFeatureTypeRepository.save(componentFeatureType);
    }

    @Override
    public List<ComponentFeatureType> getAllComponentFeatureType() {
        return componentFeatureTypeRepository.findAll();
    }

    @Override
    public ComponentFeatureType updateComponentFeatureType(ComponentFeatureType componentFeatureType, long id) {
        ComponentFeatureType existingComponentFeatureType = componentFeatureTypeRepository.findById(id).orElseThrow(RuntimeException::new);
        existingComponentFeatureType.setComponent(componentFeatureType.getComponent());
        existingComponentFeatureType.setFeatureType(componentFeatureType.getFeatureType());
        componentFeatureTypeRepository.save(existingComponentFeatureType);
        return existingComponentFeatureType;
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
}
