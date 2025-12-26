package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Feature;
import com.primebuild_online.repository.FeatureRepository;
import com.primebuild_online.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    @Override
    public Feature saveFeature(Feature feature){
        feature.setFeatureType(feature.getFeatureType());
        return featureRepository.save(feature);
    }

    @Override
    public List<Feature> getAllFeature(){
        return featureRepository.findAll();
    }

    @Override
    public Feature getFeatureById(long id) {
        Optional<Feature> feature = featureRepository.findById(id);
        if (feature.isPresent()) {
            return feature.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public Feature updateFeature(Feature feature, long id) {
        Feature existingFeature = featureRepository.findById(id).orElseThrow(RuntimeException::new);
        existingFeature.setFeatureName(feature.getFeatureName());
        existingFeature.setFeatureType(feature.getFeatureType());
        existingFeature.setItemFeatures(feature.getItemFeatures());
        featureRepository.save(existingFeature);
        return existingFeature;
    }

    @Override
    public void deleteFeature(long id){
        featureRepository.findById(id).orElseThrow(RuntimeException::new);
        featureRepository.deleteById(id);
    }

}
