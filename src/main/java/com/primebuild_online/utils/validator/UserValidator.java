package com.primebuild_online.utils.validator;

import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.SignUpMethods;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validate(User user) {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new PrimeBuildException(
                    "Username cannot be empty",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new PrimeBuildException(
                    "Email cannot be empty",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (user.getEmail().length() > 254) {
            throw new PrimeBuildException(
                    "Email is too long.",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (user.getEmail().contains(" ")) {
            throw new PrimeBuildException(
                    "Email must not contain spaces.",
                    HttpStatus.BAD_REQUEST
            );
        }

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!user.getEmail().matches(regex)) {
            throw new PrimeBuildException(
                    "Invalid email format.",
                    HttpStatus.BAD_REQUEST
            );
        }

//        if (!user.getSignUpMethod().equals(SignUpMethods.OAUTH2) && user.getPassword() != null &&
//                user.getPassword().length() < 6) {
//            throw new PrimeBuildException(
//                    "Password must be at least 6 characters",
//                    HttpStatus.BAD_REQUEST
//            );
//        }

        if (user.getRole() == null) {
            throw new PrimeBuildException(
                    "User must have a role",
                    HttpStatus.BAD_REQUEST
            );
        }

    }
}
