package com.primebuild_online.security.oauth2;

import com.primebuild_online.security.jwt.JwtUtils;
import com.primebuild_online.model.User;
import com.primebuild_online.repository.UserRepository;
import com.primebuild_online.security.services.UserDetailsImpl;
import com.primebuild_online.service.UserService;
import jakarta.servlet.http.HttpServletResponse;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, java.io.IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // Find or create user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userService.createOAuth2User(email, name));

        // Convert to UserDetailsImpl
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        // Generate JWT
        String token = jwtUtils.generateTokenFromUsername(userDetails);

        String redirectUrl =
                "http://localhost:3000/oauth2/redirect?token=" + token;

        // Return JWT
        response.setContentType("application/json");
        response.getWriter().write("""
                    {
                      "jwtToken": "%s"
                    }
                """.formatted(token));

        response.sendRedirect(redirectUrl);
    }
}