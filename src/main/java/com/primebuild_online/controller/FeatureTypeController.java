package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.FeatureTypeReqDTO;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.service.FeatureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/featureType")
public class FeatureTypeController {

    @Autowired
    private FeatureTypeService featureTypeService;

//    @PostMapping
//    public ResponseEntity<FeatureType> saveFeatureType(@RequestBody FeatureType featureType) {
//        return new ResponseEntity<>(featureTypeService.saveFeatureType(featureType), HttpStatus.CREATED);
//    }

    @PostMapping
    public ResponseEntity<FeatureType> saveFeatureTypeByComponent(@RequestBody FeatureTypeReqDTO featureTypeReqDTO) {
        return new ResponseEntity<>(featureTypeService.saveFeatureTypeByComponent(featureTypeReqDTO), HttpStatus.CREATED);
    }

//    @GetMapping
//    public List<FeatureType> getAllFeatureType() {
//        return featureTypeService.getAllFeatureType();
//    }

    @PutMapping("{id}")
    private ResponseEntity<FeatureType> updateFeatureTypeById(@PathVariable("id") long id, @RequestBody FeatureTypeReqDTO featureTypeReqDTO) {
        return new ResponseEntity<FeatureType>(featureTypeService.updateFeatureTypeById(featureTypeReqDTO, id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<FeatureType> updateFeatureTypeById(@PathVariable("id") long id) {
        return new ResponseEntity<FeatureType>(featureTypeService.getFeatureTypeById(id), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteFeatureType(@PathVariable("id") long id) {
        featureTypeService.deleteFeatureType(id);
        return new ResponseEntity<String>("FeatureType deleted Successfully", HttpStatus.OK);
    }

    @GetMapping
    public List<FeatureType> getFeatureTypesByComponent(@RequestParam(value = "component", required = false) Long componentId) {
        if (componentId != null) {
            return (featureTypeService.getFeatureTypesByComponent(componentId));
        } else {
            return featureTypeService.getAllFeatureType();
        }

    }
}
