package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.FeatureRequestDTO;
import com.primebuild_online.model.Feature;
import com.primebuild_online.service.FeatureService;
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

    @PostMapping
    public ResponseEntity<Feature> saveFeature(@RequestBody FeatureRequestDTO featureRequestDTO) {
        return new ResponseEntity<>(featureService.saveFeature(featureRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Feature> getAllFeature() {
        return featureService.getAllFeature();
    }

    @PutMapping("{id}")
    private ResponseEntity<Feature> updateFeature(@PathVariable("id") long id, @RequestBody FeatureRequestDTO featureRequestDTO) {
        return new ResponseEntity<Feature>(featureService.updateFeature(featureRequestDTO,id),HttpStatus.OK);
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

