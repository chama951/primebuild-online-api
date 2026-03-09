package com.primebuild_online.utils.validator;

import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

public class PasswordValidator {
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])" +          // lowercase
                    "(?=.*[A-Z])" +           // uppercase
                    "(?=.*\\d)" +             // digit
                    "(?=.*[@$!%*?&^#()_+=-])" + // special char
                    "[A-Za-z\\d@$!%*?&^#()_+=-]{8,}$"; // allowed chars + length

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);

    public static void validate(String password) {

        if (password == null || password.isBlank()) {
            throw new PrimeBuildException(
                    "Password cannot be empty.",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (password.contains(" ")) {
            throw new PrimeBuildException(
                    "Password must not contain spaces.",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new PrimeBuildException(
                    "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (password.matches(".*(.)\\1\\1.*")) {
            throw new PrimeBuildException(
                    "Password contains too many repeated characters.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
