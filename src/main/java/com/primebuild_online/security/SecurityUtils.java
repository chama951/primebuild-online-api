package com.primebuild_online.security;

import com.primebuild_online.security.services.UserDetailsImpl;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public static UserDetailsImpl getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new PrimeBuildException(
                    "No authenticated user found",
                    HttpStatus.CONFLICT
            );
        }


        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
