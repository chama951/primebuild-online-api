package com.primebuild_online.controller;

import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.service.ComponentFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/component_feature")
public class ComponentFeatureController {

    @Autowired
    private ComponentFeatureService componentFeatureService;

    @PostMapping
    public ResponseEntity<ComponentFeatureType> saveComponentFeature(@RequestBody ComponentFeatureType componentFeatureType) {
        return new ResponseEntity<>(componentFeatureService.saveComponentFeature(componentFeatureType), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ComponentFeatureType> getAllComponentFeature() {
        return componentFeatureService.getAllComponentFeature();
    }

    @PutMapping("{id}")
    private ResponseEntity<ComponentFeatureType> getComponentFeatureById(@PathVariable("id") long id, @RequestBody ComponentFeatureType componentFeatureType) {
        return new ResponseEntity<ComponentFeatureType>(componentFeatureService.updateComponentFeature(componentFeatureType,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ComponentFeatureType> getComponentFeatureTypeById(@PathVariable("id") long id){
        return new ResponseEntity<ComponentFeatureType>(componentFeatureService.getComponentFeatureById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteComponentFeature(@PathVariable("id") long id){
        componentFeatureService.deleteComponentFeature(id);
        return new ResponseEntity<String>("ComponentFeature deleted Successfully",HttpStatus.OK);
    }

}

