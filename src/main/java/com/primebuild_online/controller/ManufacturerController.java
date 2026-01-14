package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.ManufacturerDTO;
import com.primebuild_online.model.Manufacturer;
import com.primebuild_online.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manufacturer")
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @PostMapping
    public ResponseEntity<Manufacturer> saveComponentReq(@RequestBody ManufacturerDTO manufacturerDTO) {
        return new ResponseEntity<>(manufacturerService.saveManufacturerDTO(manufacturerDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Manufacturer> getAllComponent() {
        return manufacturerService.getAllManufacturers();
    }

    @PutMapping("{id}")
    private ResponseEntity<Manufacturer> updateManufacturerReq(@PathVariable("id") long id, @RequestBody ManufacturerDTO manufacturerDTO) {
        return new ResponseEntity<Manufacturer>(manufacturerService.updateManufacturerReq(manufacturerDTO,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable("id") long id){
        return new ResponseEntity<Manufacturer>(manufacturerService.getManufacturerById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteManufacturer(@PathVariable("id") long id){
        manufacturerService.deleteManufacturer(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Manufacturer deleted Successfully");

        return ResponseEntity.ok(response);
    }


}
