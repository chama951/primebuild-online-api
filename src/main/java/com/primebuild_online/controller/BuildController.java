package com.primebuild_online.controller;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.service.BuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/build")
public class BuildController {

    @Autowired
    private BuildService buildService;

    @PostMapping
    public ResponseEntity<Build> saveBuildReq(@RequestBody BuildReqDTO buildReqDTO){
        return new ResponseEntity<>(buildService.saveBuildReq(buildReqDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Build> getAllBuild(){
        return buildService.getAllBuild();
    }

    @PutMapping("{id}")
    public ResponseEntity<Build> updateBuildReq(@PathVariable("id") Long id, @RequestBody BuildReqDTO buildReqDTO) {
        return new ResponseEntity<>(buildService.updateBuildReq(buildReqDTO,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Build> getBuildById(@PathVariable("id") long id){
        return new ResponseEntity<Build>(buildService.getBuildById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
        public ResponseEntity<Map<String, String>> deleteBuild(@PathVariable("id")long id){
        buildService.deleteBuild(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Build Deleted Successfully");

        return ResponseEntity.ok(response);
    }

}
