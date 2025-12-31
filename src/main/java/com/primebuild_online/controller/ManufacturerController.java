package com.primebuild_online.controller;

import com.primebuild_online.model.Component;
import com.primebuild_online.model.Manufacturer;
import com.primebuild_online.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturer")
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @PostMapping
    public ResponseEntity<Manufacturer> saveComponent(@RequestBody Manufacturer manufacturer) {
        return new ResponseEntity<>(manufacturerService.saveManufacturer(manufacturer), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Manufacturer> getAllComponent() {
        return manufacturerService.getAllManufacturers();
    }

    @PutMapping("{id}")
    private ResponseEntity<Manufacturer> updateManufacturerById(@PathVariable("id") long id, @RequestBody Manufacturer manufacturer) {
        return new ResponseEntity<Manufacturer>(manufacturerService.updateManufacturer(manufacturer,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable("id") long id){
        return new ResponseEntity<Manufacturer>(manufacturerService.getManufacturerById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteManufacturer(@PathVariable("id") long id){
        manufacturerService.deleteManufacturer(id);
        return new ResponseEntity<String>("Manufacturer deleted Successfully",HttpStatus.OK);
    }


}
