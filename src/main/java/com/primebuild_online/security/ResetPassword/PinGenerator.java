package com.primebuild_online.security.ResetPassword;

import java.security.SecureRandom;

public class PinGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generatePin(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
