package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.ItemFeatureDTO;
import com.primebuild_online.model.Feature;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemFeature;
import com.primebuild_online.repository.ItemFeatureRepository;
import com.primebuild_online.service.FeatureService;
import com.primebuild_online.service.ItemFeatureService;
import com.primebuild_online.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemFeatureServiceImpl implements ItemFeatureService {
    private final ItemFeatureRepository itemFeatureRepository;
    private final FeatureService featureService;
    private final ItemService itemService;

    public ItemFeatureServiceImpl(ItemService itemService, ItemFeatureRepository itemFeatureRepository, FeatureService featureService) {
        this.itemService = itemService;
        this.itemFeatureRepository = itemFeatureRepository;
        this.featureService = featureService;
    }

    @Override
    public ItemFeature saveItemFeatureRequest(ItemFeatureDTO itemFeatureDTO) {

        ItemFeature newItemFeature = new ItemFeature();

        if (itemFeatureDTO.getItemId() != null) {
            Item item = itemService.getItemById(itemFeatureDTO.getItemId());
            newItemFeature.setItem(item);
        }
        if (itemFeatureDTO.getFeatureId() != null) {
            Feature feature = featureService.getFeatureById(itemFeatureDTO.getFeatureId());
            newItemFeature.setFeature(feature);
        }
        newItemFeature.setSlotCount(itemFeatureDTO.getSlotCount());
        return itemFeatureRepository.save(newItemFeature);
    }

    @Override
    public List<ItemFeature> getAllItemFeature() {
        return itemFeatureRepository.findAll();
    }

    @Override
    public List<ItemFeature> getItemFeatureByItem(Long itemId) {
        return itemFeatureRepository.findAllByItemId(itemId);
    }


    @Override
    public ItemFeature updateItemFeatureRequest(ItemFeatureDTO itemFeatureDTO, long id) {

        ItemFeature itemFeatureInDb = itemFeatureRepository.findById(id).orElseThrow(RuntimeException::new);

        if (itemFeatureDTO.getItemId() != null) {
            Item item = itemService.getItemById(itemFeatureDTO.getItemId());
            itemFeatureInDb.setItem(item);
        }
        if (itemFeatureDTO.getFeatureId() != null) {
            Feature feature = featureService.getFeatureById(itemFeatureDTO.getFeatureId());
            itemFeatureInDb.setFeature(feature);
        }

        itemFeatureInDb.setSlotCount(itemFeatureDTO.getSlotCount());
        itemFeatureRepository.save(itemFeatureInDb);

        return itemFeatureInDb;
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

    @Override
    public void saveItemFeature(ItemFeature itemFeature) {
        itemFeatureRepository.save(itemFeature);
    }

    @Override
    public void deleteAllByFeatureId(long id) {
        itemFeatureRepository.deleteAllByItemId(id);
    }

}
