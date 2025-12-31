package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.FeatureTypeRepository;
import com.primebuild_online.service.FeatureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureTypeServiceImpl implements FeatureTypeService {

    @Autowired
    private FeatureTypeRepository featureTypeRepository;

    @Override
    public FeatureType saveFeatureType(FeatureType featureType){
        return featureTypeRepository.save(featureType);
    }

    @Override
    public List<FeatureType> getAllFeatureType(){
        return featureTypeRepository.findAll();
    }

    @Override
    public FeatureType getFeatureTypeById(long id) {
        Optional<FeatureType> featureType = featureTypeRepository.findById(id);
        if (featureType.isPresent()) {
            return featureType.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public FeatureType updateFeatureType(FeatureType featureType, long id) {
        FeatureType existingFeatureType = featureTypeRepository.findById(id).orElseThrow(RuntimeException::new);
        existingFeatureType.setFeatureTypeName(featureType.getFeatureTypeName());
        existingFeatureType.setFeatureList(featureType.getFeatureList());
        existingFeatureType.setComponentFeatureTypes(featureType.getComponentFeatureTypes());
        featureTypeRepository.save(existingFeatureType);
        return existingFeatureType;
    }

    @Override
    public void deleteFeatureType(long id){
        featureTypeRepository.findById(id).orElseThrow(RuntimeException::new);
        featureTypeRepository.deleteById(id);
    }
}
