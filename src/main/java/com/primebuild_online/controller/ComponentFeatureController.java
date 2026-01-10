package com.primebuild_online.controller;

import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.service.ComponentFeatureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/component_feature")
public class ComponentFeatureController {

    @Autowired
    private ComponentFeatureTypeService componentFeatureTypeService;

    @PostMapping
    public ResponseEntity<ComponentFeatureType> saveComponentFeatureType(@RequestBody ComponentFeatureType componentFeatureType) {
        return new ResponseEntity<>(componentFeatureTypeService.saveComponentFeatureType(componentFeatureType), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ComponentFeatureType> getAllComponentFeature() {
        return componentFeatureTypeService.getAllComponentFeatureType();
    }

    @PutMapping("{id}")
    private ResponseEntity<ComponentFeatureType> getComponentFeatureById(@PathVariable("id") long id, @RequestBody ComponentFeatureType componentFeatureType) {
        return new ResponseEntity<ComponentFeatureType>(componentFeatureTypeService.updateComponentFeatureType(componentFeatureType,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ComponentFeatureType> getComponentFeatureTypeById(@PathVariable("id") long id){
        return new ResponseEntity<ComponentFeatureType>(componentFeatureTypeService.getComponentFeatureTypeById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteComponentFeature(@PathVariable("id") long id){
        componentFeatureTypeService.deleteComponentFeatureType(id);
        return new ResponseEntity<String>("ComponentFeature deleted Successfully",HttpStatus.OK);
    }

}

