package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.FeatureReqDTO;
import com.primebuild_online.model.Feature;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.repository.FeatureRepository;
import com.primebuild_online.service.FeatureService;
import com.primebuild_online.service.FeatureTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureServiceImpl implements FeatureService {
    private final FeatureRepository featureRepository;
    private final FeatureTypeService featureTypeService;

    public FeatureServiceImpl(FeatureRepository featureRepository,
                              FeatureTypeService featureTypeService) {
        this.featureRepository = featureRepository;
        this.featureTypeService = featureTypeService;
    }

    @Override
    public Feature saveFeatureReq(FeatureReqDTO featureReqDTO){
        Feature feature= new Feature();
        feature.setFeatureName(featureReqDTO.getFeatureName());
        if(featureReqDTO.getFeatureTypeId()!=null){
            FeatureType featureType = featureTypeService.getFeatureTypeById(featureReqDTO.getFeatureTypeId());
            feature.setFeatureType(featureType);
        }
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
    public Feature updateFeatureReq(FeatureReqDTO featureReqDTO, long id) {
        Feature featureInDb = featureRepository.findById(id).orElseThrow(RuntimeException::new);
        featureInDb.setFeatureName(featureReqDTO.getFeatureName());

        if(featureReqDTO.getFeatureTypeId()!=null){
            FeatureType featureType = featureTypeService.getFeatureTypeById(featureReqDTO.getFeatureTypeId());
            featureInDb.setFeatureType(featureType);
        }
        featureRepository.save(featureInDb);
        return featureInDb;
    }

    @Override
    public void deleteFeature(long id){
        featureRepository.findById(id).orElseThrow(RuntimeException::new);
        featureRepository.deleteById(id);
    }

    @Override
    public List<Feature> getFeatureListByFeatureType(Long featureTypeId) {
        return featureRepository.findAllByFeatureTypeId(featureTypeId);
    }

    public List<Feature> getFeaturesListByItem(Long itemId){
        return null;
    }

}
