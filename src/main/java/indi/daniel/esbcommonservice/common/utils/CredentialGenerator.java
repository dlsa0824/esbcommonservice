package indi.daniel.esbcommonservice.common.utils;

import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class CredentialGenerator {

    private final int numBytes = 43;

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generateClientId() {
        return UUID.randomUUID().toString();
    }
    
    public String generateClientSecret() {
        StringBuilder stringBuilder = new StringBuilder(numBytes);
        for (int i = 0; i < numBytes; i++) {
            int index = SECURE_RANDOM.nextInt(ALPHANUMERIC.length());
            stringBuilder.append(ALPHANUMERIC.charAt(index));
        }
        return stringBuilder.toString();

    }
}
