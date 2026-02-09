package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.RoleDTO;
import com.primebuild_online.model.Role;
import com.primebuild_online.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> saveRole(@RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(roleService.saveRole(roleDTO), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") Long id,@RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<Role>(roleService.updateRole(roleDTO,id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id) {
        return new ResponseEntity<Role>(roleService.getRoleById(id), HttpStatus.OK);
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteRole(@PathVariable("id") Long id){
        roleService.deleteRole(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Role deleted Successfully");

        return ResponseEntity.ok(response);
    }

}
