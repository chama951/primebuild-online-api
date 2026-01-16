package com.primebuild_online.controller;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.DTO.ComponentReqDTO;
import com.primebuild_online.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/component")
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @PostMapping
    public ResponseEntity<Component> saveComponentFeatureTypeReq(@RequestBody ComponentReqDTO componentReqDTO) {
        return new ResponseEntity<>(componentService.saveComponentReq(componentReqDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Component> getAllComponent() {
        return componentService.getAllComponent();
    }

    @PutMapping("{id}")
    private ResponseEntity<Component> updateComponentReq(@PathVariable("id") Long id, @RequestBody ComponentReqDTO componentReqDTO) {
        return new ResponseEntity<Component>(componentService.updateComponentReq(componentReqDTO, id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Component> getComponentById(@PathVariable("id") Long id) {
        return new ResponseEntity<Component>(componentService.getComponentById(id), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteComponent(@PathVariable("id") Long id) {
        componentService.deleteComponent(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Component deleted Successfully");

        return ResponseEntity.ok(response);
    }
}
