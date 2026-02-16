package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.UserDTO;
import com.primebuild_online.model.User;
import com.primebuild_online.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> saveUserReq(@RequestBody UserDTO userDTO) {
        User user = userService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    public List<User> getAllUsers(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "email", required = false) String email,
                                  @RequestParam(value = "type", required = false) String type) {
        if (username != null) {
            return userService.getByUsername(username);
        }
        if (email != null) {
            return userService.getByEmail(email);
        }
        if (type != null) {
            if (type.equals("customer")) {
                return userService.getAllCustomers();
            }
            if (type.equals("staff")) {
                return userService.getAllStaff();
            }
        }
        return userService.getAllUsers();
    }

    @PutMapping("{id}")
    private ResponseEntity<User> updateUserReq(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        User user = userService.updateUser(userDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted Successfully");

        return ResponseEntity.ok(response);
    }
}
