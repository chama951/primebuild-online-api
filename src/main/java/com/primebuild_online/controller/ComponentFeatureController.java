package com.primebuild_online.controller;

import com.primebuild_online.model.ComponentFeature;
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
    public ResponseEntity<ComponentFeature> saveComponentFeature(@RequestBody ComponentFeature componentFeature) {
        return new ResponseEntity<>(componentFeatureService.saveComponentFeature(componentFeature), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ComponentFeature> getAllComponentFeature() {
        return componentFeatureService.getAllComponentFeature();
    }

    @PutMapping("{id}")
    private ResponseEntity<ComponentFeature> getComponentFeatureById(@PathVariable("id") long id, @RequestBody ComponentFeature componentFeature) {
        return new ResponseEntity<ComponentFeature>(componentFeatureService.updateComponentFeature(componentFeature,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ComponentFeature> getComponentFeatureTypeById(@PathVariable("id") long id){
        return new ResponseEntity<ComponentFeature>(componentFeatureService.getComponentFeatureById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteComponentFeature(@PathVariable("id") long id){
        componentFeatureService.deleteComponentFeature(id);
        return new ResponseEntity<String>("ComponentFeature deleted Successfully",HttpStatus.OK);
    }

}

