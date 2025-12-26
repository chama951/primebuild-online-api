package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.ComponentFeature;
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
    public ComponentFeature saveComponentFeature(ComponentFeature componentFeature){
        componentFeature.setComponent(componentFeature.getComponent());
        componentFeature.setFeatureType(componentFeature.getFeatureType());
        return componentFeatureRepository.save(componentFeature);
    }

    @Override
    public List<ComponentFeature> getAllComponentFeature() {
        return componentFeatureRepository.findAll();
    }

    @Override
    public ComponentFeature updateComponentFeature(ComponentFeature componentFeature, long id) {
        ComponentFeature existingComponentFeature = componentFeatureRepository.findById(id).orElseThrow(RuntimeException::new);
        existingComponentFeature.setComponent(componentFeature.getComponent());
        existingComponentFeature.setFeatureType(componentFeature.getFeatureType());
        componentFeatureRepository.save(existingComponentFeature);
        return existingComponentFeature;
    }

    @Override
    public ComponentFeature getComponentFeatureById(long id) {
        Optional<ComponentFeature> componentFeature = componentFeatureRepository.findById(id);
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
