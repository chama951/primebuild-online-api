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
        Role role = roleService.saveRole(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @PutMapping("{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") Long id, @RequestBody RoleDTO roleDTO) {
        Role role = roleService.updateRole(roleDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @GetMapping("{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Role deleted Successfully");

        return ResponseEntity.ok(response);
    }

}
