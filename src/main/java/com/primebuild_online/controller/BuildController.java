package com.primebuild_online.controller;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.DTO.BuildRequestDTO;
import com.primebuild_online.model.DTO.ItemListDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.service.BuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/build")
public class BuildController {

    @Autowired
    private BuildService buildService;

    @PutMapping("/{id}/items")
    public  ResponseEntity<Build> addNewItems(@PathVariable("id") long id, @RequestBody ItemListDTO items) {
        return new ResponseEntity<>(buildService.addNewItems(items,id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Build> saveBuild(@RequestBody BuildRequestDTO buildRequest){
        return new ResponseEntity<>(buildService.saveBuild(buildRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Build> getAllBuild(){
        return buildService.getAllBuild();
    }

    @PutMapping("{id}")
    public ResponseEntity<Build> updateBuildById(@PathVariable("id") long id, @RequestBody BuildRequestDTO buildRequest) {
        return new ResponseEntity<>(buildService.updateBuild(buildRequest,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Build> getBuildById(@PathVariable("id") long id){
        return new ResponseEntity<Build>(buildService.getBuildById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
        public ResponseEntity<String> deleteBuild(@PathVariable("id")long id){
        buildService.deleteBuild(id);
        return new ResponseEntity<String>("Build Deleted Successfully",HttpStatus.OK);
    }

}
