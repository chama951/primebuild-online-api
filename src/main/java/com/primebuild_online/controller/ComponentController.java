package com.primebuild_online.controller;

import com.primebuild_online.model.Component;
import com.primebuild_online.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/component")
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    @PostMapping
    public ResponseEntity<Component> saveComponent(@RequestBody Component component) {
        return new ResponseEntity<>(componentService.saveComponent(component), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Component> getAllComponent() {
        return componentService.getAllComponent();
    }

    @PutMapping("{id}")
    private ResponseEntity<Component> updateComponentById(@PathVariable("id") long id, @RequestBody Component component) {
        return new ResponseEntity<Component>(componentService.updateComponent(component,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Component> getComponentById(@PathVariable("id") long id){
        return new ResponseEntity<Component>(componentService.getComponentById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteComponent(@PathVariable("id") long id){
        componentService.deleteComponent(id);
        return new ResponseEntity<String>("Component deleted Successfully",HttpStatus.OK);
    }
}
