package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.ItemFeature;
import com.primebuild_online.repository.FeatureRepository;
import com.primebuild_online.repository.ItemFeatureRepository;
import com.primebuild_online.service.ItemFeatureSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemFeatureServiceImpl implements ItemFeatureSevice {

    @Autowired
    private ItemFeatureRepository itemFeatureRepository;

    @Override
    public ItemFeature saveItemFeature(ItemFeature itemFeature) {
        itemFeature.setItem(itemFeature.getItem());
        itemFeature.setFeature(itemFeature.getFeature());
        return itemFeatureRepository.save(itemFeature);
    }

    @Override
    public List<ItemFeature> getAllItemFeature() {
        return itemFeatureRepository.findAll();
    }

    @Override
    public ItemFeature updateItemFeature(ItemFeature itemFeature, long id) {
        ItemFeature existingItemFeature = itemFeatureRepository.findById(id).orElseThrow(RuntimeException::new);
        existingItemFeature.setFeature(itemFeature.getFeature());
        existingItemFeature.setItem(itemFeature.getItem());
        itemFeatureRepository.save(existingItemFeature);
        return existingItemFeature;
    }

    @Override
    public ItemFeature getItemFeatureById(long id) {
        Optional<ItemFeature> itemFeature = itemFeatureRepository.findById(id);
        if (itemFeature.isPresent()) {
            return itemFeature.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteAllByItemId(long itemId) {
        itemFeatureRepository.deleteByItemId(itemId);

    }

    @Override
    public void deleteItemFeature(long id) {
        itemFeatureRepository.findById(id).orElseThrow(RuntimeException::new);
        itemFeatureRepository.deleteById(id);
    }

    @Override
    public List<ItemFeature> findByItemId(Long itemId) {
        return itemFeatureRepository.findByItemId(itemId);
    }

}
