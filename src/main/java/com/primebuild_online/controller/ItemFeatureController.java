package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.ItemFeatureDTO;
import com.primebuild_online.model.ItemFeature;
import com.primebuild_online.service.ItemFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/item_feature")
public class ItemFeatureController {

    @Autowired
    private ItemFeatureService itemFeatureService;

    @GetMapping("/item={itemId}")
    public List<ItemFeature> getItemFeaturesByItemId(
            @PathVariable("itemId") Long itemId) {
        return itemFeatureService.findByItemId(itemId);
    }

    @PostMapping
    public ResponseEntity<ItemFeature> saveItemFeature(@RequestBody ItemFeatureDTO itemFeatureDTO) {
        return new ResponseEntity<>(itemFeatureService.saveItemFeatureRequest(itemFeatureDTO), HttpStatus.CREATED);
    }

//    @GetMapping
//    public List<ItemFeature> getAllItemFeature() {
//        return itemFeatureService.getAllItemFeature();
//    }

    @GetMapping
    public List<ItemFeature> getItemFeatureByItem(
            @RequestParam(value = "item", required = false) Long itemId) {
        if (itemId != null) {
            return itemFeatureService.getItemFeatureByItem(itemId);
        } else {
            return itemFeatureService.getAllItemFeature();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ItemFeature> updateItemFeatureById(@PathVariable("id") long id,
                                                             @RequestBody ItemFeatureDTO itemFeatureDTO) {
        return new ResponseEntity<ItemFeature>(itemFeatureService.updateItemFeatureRequest(itemFeatureDTO, id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ItemFeature> getItemFeatureById(@PathVariable("id") long id) {
        return new ResponseEntity<ItemFeature>(itemFeatureService.getItemFeatureById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteItemFeature(@PathVariable("id") long id) {
        itemFeatureService.deleteItemFeature(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "ItemFeature deleted Successfully");

        return ResponseEntity.ok(response);
    }
}
