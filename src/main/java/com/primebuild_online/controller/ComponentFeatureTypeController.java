package com.primebuild_online.controller;

import com.primebuild_online.model.ComponentFeatureType;
import com.primebuild_online.model.DTO.ComponentFeatureTypeReqDTO;
import com.primebuild_online.service.ComponentFeatureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/component_feature_type")
public class ComponentFeatureTypeController {

    @Autowired
    private ComponentFeatureTypeService componentFeatureTypeService;

    @PostMapping
    public ResponseEntity<ComponentFeatureType> saveComponentFeatureTypeReq(@RequestBody ComponentFeatureTypeReqDTO componentFeatureTypeReqDTO) {
        return new ResponseEntity<>(componentFeatureTypeService.saveComponentFeatureTypeReq(componentFeatureTypeReqDTO), HttpStatus.CREATED);

    }

    @PutMapping("{id}")
    private ResponseEntity<ComponentFeatureType> updateComponentFeatureTypeReq(@PathVariable("id") long id, @RequestBody ComponentFeatureTypeReqDTO componentFeatureTypeReqDTO) {
        return new ResponseEntity<ComponentFeatureType>(componentFeatureTypeService.updateComponentFeatureTypeReq(componentFeatureTypeReqDTO,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ComponentFeatureType> getComponentFeatureTypeById(@PathVariable("id") long id){
        return new ResponseEntity<ComponentFeatureType>(componentFeatureTypeService.getComponentFeatureTypeById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteComponentFeatureType(@PathVariable("id") long id){
        componentFeatureTypeService.deleteComponentFeatureType(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "ComponentFeature deleted Successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<ComponentFeatureType> getFeatureComponentTypesByComponent(@RequestParam(value = "component", required = false) Long componentId) {
        if (componentId != null) {
            return (componentFeatureTypeService.getComponentFeatureTypesByComponentId(componentId));
        } else {
            return componentFeatureTypeService.getAllComponentFeatureType();
        }

    }

}

