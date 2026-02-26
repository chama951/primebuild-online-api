package com.primebuild_online.security.oauth2;

import com.primebuild_online.security.jwt.JwtUtils;
import com.primebuild_online.model.User;
import com.primebuild_online.repository.UserRepository;
import com.primebuild_online.security.services.UserDetailsImpl;
import com.primebuild_online.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public OAuth2AuthenticationSuccessHandler(UserRepository userRepository,
                                              JwtUtils jwtUtils,
                                              UserService userService) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // Find or create user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userService.createOAuth2User(email, name));

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        String token = jwtUtils.generateTokenFromUsername(userDetails);

        String roles = user.getRole().getRolePrivilegeList()
                .stream()
                .map(rp -> rp.getPrivilege().name())
                .collect(Collectors.joining(","));

        String redirectPath = userService.checkIsACustomer(user) ? "/home" : "/dashboard";

        String redirectUrl = String.format(
                "http://localhost:3000/oauth2-success?token=%s&username=%s&roles=%s&redirectUrl=%s",
                URLEncoder.encode(token, StandardCharsets.UTF_8),
                URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8),
                URLEncoder.encode(roles, StandardCharsets.UTF_8),
                URLEncoder.encode("http://localhost:3000" + redirectPath, StandardCharsets.UTF_8) // <-- full URL
        );

        response.sendRedirect(redirectUrl);
    }
}