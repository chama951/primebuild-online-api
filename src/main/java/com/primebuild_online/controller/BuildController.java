package com.primebuild_online.controller;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.DTO.BuildReqDTO;
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

    @PostMapping
    public ResponseEntity<Build> saveBuild(@RequestBody BuildReqDTO buildRequest){
        return new ResponseEntity<>(buildService.saveBuild(buildRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Build> getAllBuild(){
        return buildService.getAllBuild();
    }

    @PutMapping("{id}")
    public ResponseEntity<Build> updateBuildById(@PathVariable("id") Long id, @RequestBody BuildReqDTO buildRequest) {
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
