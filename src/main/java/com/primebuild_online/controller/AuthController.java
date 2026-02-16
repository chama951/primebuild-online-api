package com.primebuild_online.controller;

import com.primebuild_online.security.jwt.JwtUtils;
import com.primebuild_online.security.jwt.LoginRequestDTO;
import com.primebuild_online.security.jwt.LoginResponseDTO;
import com.primebuild_online.model.DTO.UserDTO;
import com.primebuild_online.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO,
                                      @RequestParam(value = "type", required = false) String type) {
        if (type != null) {
            if (type.equals("staff")) {
                userService.saveUser(userDTO);
            }
            if (type.equals("customer")) {
                userService.signupCustomer(userDTO);
            }
        }
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername(userDTO.getUsername());
        loginRequestDTO.setPassword(userDTO.getPassword());
        return authenticateUser(loginRequestDTO);
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponseDTO response = new LoginResponseDTO(userDetails.getUsername(), roles, jwtToken);

        return ResponseEntity.ok(response);
    }

}
