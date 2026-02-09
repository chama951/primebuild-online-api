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
        return new ResponseEntity<>(userService.saveUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<User> getAllUsers(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "email", required = false) String email,
                                  @RequestParam(value = "is_customer", required = false) boolean isCustomer) {
        if (username != null) {
            return userService.getByUsername(username);
        }
        if (email != null) {
            return userService.getByEmail(email);
        }
        if (isCustomer) {
            return userService.getAllCustomers();
        }
        if (!isCustomer) {
            return userService.getAllStaff();
        }
        return userService.getAllUsers();
    }

    @PutMapping("{id}")
    private ResponseEntity<User> updateUserReq(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        return new ResponseEntity<User>(userService.updateUser(userDTO, id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return new ResponseEntity<User>(userService.getUserById(id), HttpStatus.OK);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted Successfully");

        return ResponseEntity.ok(response);
    }
}
