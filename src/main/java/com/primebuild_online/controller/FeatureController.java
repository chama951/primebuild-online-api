package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.FeatureReqDTO;
import com.primebuild_online.model.Feature;
import com.primebuild_online.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feature")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    @PostMapping
    public ResponseEntity<Feature> saveFeatureReq(@RequestBody FeatureReqDTO featureReqDTO) {
        return new ResponseEntity<>(featureService.saveFeatureReq(featureReqDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Feature> getFeatureList(@RequestParam(value = "feature_type", required = false) Long featureTypeId) {
        if (featureTypeId != null) {
//            byFeatureType
            return (featureService.getFeatureListByFeatureType(featureTypeId));
        } else {
            return featureService.getAllFeature();
        }

    }

    @PutMapping("{id}")
    private ResponseEntity<Feature> updateFeatureReq(@PathVariable("id") long id, @RequestBody FeatureReqDTO featureReqDTO) {
        return new ResponseEntity<Feature>(featureService.updateFeatureReq(featureReqDTO, id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Feature> getFeatureTypeById(@PathVariable("id") long id) {
        return new ResponseEntity<Feature>(featureService.getFeatureById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteFeature(@PathVariable("id") long id) {
        featureService.deleteFeature(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Feature deleted Successfully");

        return ResponseEntity.ok(response);
    }
}

