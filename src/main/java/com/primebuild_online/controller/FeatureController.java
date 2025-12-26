package com.primebuild_online.controller;

import com.primebuild_online.model.Feature;
import com.primebuild_online.service.FeatureService;
import com.primebuild_online.service.ItemFeatureSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feature")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    @Autowired
    private ItemFeatureSevice itemFeatureSevice;

    @PostMapping
    public ResponseEntity<Feature> saveFeature(@RequestBody Feature feature) {
        return new ResponseEntity<>(featureService.saveFeature(feature), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Feature> getAllFeature() {
        return featureService.getAllFeature();
    }

    @PutMapping("{id}")
    private ResponseEntity<Feature> getFeatureById(@PathVariable("id") long id, @RequestBody Feature feature) {
        return new ResponseEntity<Feature>(featureService.updateFeature(feature,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Feature> getFeatureTypeById(@PathVariable("id") long id){
        return new ResponseEntity<Feature>(featureService.getFeatureById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteFeature(@PathVariable("id") long id){
        featureService.deleteFeature(id);
        return new ResponseEntity<String>("Feature deleted Successfully",HttpStatus.OK);
    }
}

