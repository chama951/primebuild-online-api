package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.repository.ComponentFeatureRepository;
import com.primebuild_online.service.ComponentFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentFeatureServiceImpl implements ComponentFeatureService {

    @Autowired
    private ComponentFeatureRepository componentFeatureRepository;

    @Override
    public ComponentFeatureType saveComponentFeature(ComponentFeatureType componentFeatureType){
        componentFeatureType.setComponent(componentFeatureType.getComponent());
        componentFeatureType.setFeatureType(componentFeatureType.getFeatureType());
        return componentFeatureRepository.save(componentFeatureType);
    }

    @Override
    public List<ComponentFeatureType> getAllComponentFeature() {
        return componentFeatureRepository.findAll();
    }

    @Override
    public ComponentFeatureType updateComponentFeature(ComponentFeatureType componentFeatureType, long id) {
        ComponentFeatureType existingComponentFeatureType = componentFeatureRepository.findById(id).orElseThrow(RuntimeException::new);
        existingComponentFeatureType.setComponent(componentFeatureType.getComponent());
        existingComponentFeatureType.setFeatureType(componentFeatureType.getFeatureType());
        componentFeatureRepository.save(existingComponentFeatureType);
        return existingComponentFeatureType;
    }

    @Override
    public ComponentFeatureType getComponentFeatureById(long id) {
        Optional<ComponentFeatureType> componentFeature = componentFeatureRepository.findById(id);
        if (componentFeature.isPresent()) {
            return componentFeature.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteComponentFeature(long id) {
        componentFeatureRepository.findById(id).orElseThrow(RuntimeException::new);
        componentFeatureRepository.deleteById(id);
    }
}
