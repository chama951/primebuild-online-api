package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.ResetPasswordDTO;
import com.primebuild_online.model.User;
import com.primebuild_online.repository.UserRepository;
import com.primebuild_online.security.jwt.JwtUtils;
import com.primebuild_online.security.jwt.LoginRequestDTO;
import com.primebuild_online.security.jwt.LoginResponseDTO;
import com.primebuild_online.model.DTO.UserDTO;
import com.primebuild_online.security.services.UserDetailsImpl;
import com.primebuild_online.service.ResetPasswordService;
import com.primebuild_online.service.UserService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/api/auth/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO,
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

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        } catch (AuthenticationException exception) {
            throw new PrimeBuildException(
                    "Bad credentials",
                    HttpStatus.NOT_FOUND);
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

    @GetMapping("/api/auth/self")
    public ResponseEntity<?> getSelf(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.getUserById(userDetails.getId());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/auth/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        Map<String, String> response = new HashMap<>();
        if (userRepository.existsByEmail(resetPasswordDTO.getEmail())) {
            resetPasswordService.createAndSendPin(resetPasswordDTO.getEmail());
            response.put("message", "PIN sent to your email!");
        } else {
            response.put("message", "Email not found!");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/auth/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        Map<String, String> response = new HashMap<>();
        boolean success = resetPasswordService.resetPassword(resetPasswordDTO);
        if (success) {
            response.put("message", "Password reset successful!");
        } else {
            response.put("message", "Password reset successful!");
        }
        return ResponseEntity.ok(response);
    }
}
