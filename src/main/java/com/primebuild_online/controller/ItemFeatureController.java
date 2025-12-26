package com.primebuild_online.controller;

import com.primebuild_online.model.ItemFeature;
import com.primebuild_online.service.ItemFeatureSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item_feature")
public class ItemFeatureController {

    @Autowired
    private ItemFeatureSevice itemFeatureService;

    @GetMapping("/item={itemId}")
    public List<ItemFeature> getItemFeaturesByItemId(
            @PathVariable("itemId") Long itemId) {
        return itemFeatureService.findByItemId(itemId);
    }

    @PostMapping
    public ResponseEntity<ItemFeature> saveItemFeature(@RequestBody ItemFeature itemFeature) {
        return new ResponseEntity<>(itemFeatureService.saveItemFeature(itemFeature), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ItemFeature> getAllItemFeature() {
        return itemFeatureService.getAllItemFeature();
    }

    @PutMapping("{id}")
    private ResponseEntity<ItemFeature> updateItemFeatureById(@PathVariable("id") long id, @RequestBody ItemFeature itemFeature) {
        return new ResponseEntity<ItemFeature>(itemFeatureService.updateItemFeature(itemFeature,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ItemFeature> getItemFeatureById(@PathVariable("id") long id){
        return new ResponseEntity<ItemFeature>(itemFeatureService.getItemFeatureById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteItemFeature(@PathVariable("id") long id){
        itemFeatureService.deleteItemFeature(id);
        return new ResponseEntity<String>("ItemFeature deleted Successfully",HttpStatus.OK);
    }
}
