package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.ItemFeatureDTO;
import com.primebuild_online.model.Feature;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemFeature;
import com.primebuild_online.repository.ItemFeatureRepository;
import com.primebuild_online.service.FeatureService;
import com.primebuild_online.service.ItemFeatureService;
import com.primebuild_online.service.ItemService;
import com.primebuild_online.utils.validator.ItemFeatureValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemFeatureServiceImpl implements ItemFeatureService {
    private final ItemFeatureRepository itemFeatureRepository;
    private final FeatureService featureService;
    private final ItemService itemService;
    private final ItemFeatureValidator itemFeatureValidator;

    public ItemFeatureServiceImpl(
            ItemService itemService,
            ItemFeatureRepository itemFeatureRepository,
            FeatureService featureService,
            ItemFeatureValidator itemFeatureValidator) {
        this.itemService = itemService;
        this.itemFeatureRepository = itemFeatureRepository;
        this.featureService = featureService;
        this.itemFeatureValidator = itemFeatureValidator;
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
        itemFeatureValidator.validate(newItemFeature);
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
    public ItemFeature updateItemFeatureRequest(ItemFeatureDTO itemFeatureDTO, Long id) {

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

        itemFeatureValidator.validate(itemFeatureInDb);
        itemFeatureRepository.save(itemFeatureInDb);

        return itemFeatureInDb;
    }

    @Override
    public ItemFeature getItemFeatureById(Long id) {
        Optional<ItemFeature> itemFeature = itemFeatureRepository.findById(id);
        if (itemFeature.isPresent()) {
            return itemFeature.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteAllByItemId(Long itemId) {
        itemFeatureRepository.deleteByItemId(itemId);
    }

    @Override
    public void deleteItemFeature(Long id) {
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
    public void deleteAllByFeatureId(Long id) {
        itemFeatureRepository.deleteAllByFeatureId(id);
    }

}
