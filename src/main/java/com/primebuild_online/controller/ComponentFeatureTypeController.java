package com.primebuild_online.controller;

import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.ComponentFeatureTypeReqDTO;
import com.primebuild_online.service.ComponentFeatureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/component_feature_type")
public class ComponentFeatureTypeController {

    @Autowired
    private ComponentFeatureTypeService componentFeatureTypeService;

    @PostMapping
    public ResponseEntity<ComponentFeatureType> saveComponentFeatureTypeReq(@RequestBody ComponentFeatureTypeReqDTO componentFeatureTypeReqDTO) {
        return new ResponseEntity<>(componentFeatureTypeService.saveComponentFeatureTypeReq(componentFeatureTypeReqDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ComponentFeatureType> getAllComponentFeatureType() {
        return componentFeatureTypeService.getAllComponentFeatureType();
    }

    @PutMapping("{id}")
    private ResponseEntity<ComponentFeatureType> updateComponentFeatureType(@PathVariable("id") long id, @RequestBody ComponentFeatureTypeReqDTO componentFeatureTypeReqDTO) {
        return new ResponseEntity<ComponentFeatureType>(componentFeatureTypeService.updateComponentFeatureType(componentFeatureTypeReqDTO,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ComponentFeatureType> getComponentFeatureTypeById(@PathVariable("id") long id){
        return new ResponseEntity<ComponentFeatureType>(componentFeatureTypeService.getComponentFeatureTypeById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteComponentFeatureType(@PathVariable("id") long id){
        componentFeatureTypeService.deleteComponentFeatureType(id);
        return new ResponseEntity<String>("ComponentFeature deleted Successfully",HttpStatus.OK);
    }

}

