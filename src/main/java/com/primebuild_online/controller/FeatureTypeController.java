package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.FeatureTypeReqDTO;
import com.primebuild_online.model.FeatureType;
import com.primebuild_online.service.FeatureTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feature_type")
public class FeatureTypeController {

    @Autowired
    private FeatureTypeService featureTypeService;

    @PostMapping
    public ResponseEntity<FeatureType> saveFeatureTypeReq(@RequestBody FeatureTypeReqDTO featureTypeReqDTO) {
        return new ResponseEntity<>(featureTypeService.saveFeatureTypeReq(featureTypeReqDTO), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    private ResponseEntity<FeatureType> updateFeatureTypeReq(@PathVariable("id") Long id, @RequestBody FeatureTypeReqDTO featureTypeReqDTO) {
        return new ResponseEntity<FeatureType>(featureTypeService.updateFeatureTypeReq(featureTypeReqDTO, id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<FeatureType> getFeatureTypeById(@PathVariable("id") Long id) {
        return new ResponseEntity<FeatureType>(featureTypeService.getFeatureTypeById(id), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteFeatureType(@PathVariable("id") Long id) {
        featureTypeService.deleteFeatureType(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "FeatureType deleted Successfully");

        return ResponseEntity.ok(response);
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
