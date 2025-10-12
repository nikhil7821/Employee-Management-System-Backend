package com.task.demo.project.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class JwtSecretGenerator {

    public static void main(String[] args) {
        // Method 1: Generate cryptographically secure random key
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Secret = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("=== GENERATED JWT SECRETS ===");
        System.out.println("Base64 Secret: " + base64Secret);
        System.out.println("Length: " + base64Secret.length() + " characters");

        // Method 2: Create from custom string (minimum 256 bits for HS256)
        String customSecret = "MyCustomJWTSecretKeyThatIsVerySecureAndAtLeast256BitsLong123456789";
        String customBase64 = Base64.getEncoder().encodeToString(customSecret.getBytes());

        System.out.println("\nCustom Base64 Secret: " + customBase64);
        System.out.println("Length: " + customBase64.length() + " characters");

        // Method 3: Simple alphanumeric secret (easiest)
        String simpleSecret = "myJWTSecretKeyForApplication2024VerySecureAndLongEnoughForHS256Algorithm";
        System.out.println("\nSimple Secret: " + simpleSecret);
        System.out.println("Length: " + simpleSecret.length() + " characters");
    }
}
