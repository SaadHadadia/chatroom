package com.example.chatroom.config.security.encryption;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@Data
public class AESEncryption {

    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 256; // AES Key size in bits
    private static final int TAG_LENGTH_BIT = 128; // GCM tag length in bits
    private static final int IV_LENGTH_BYTE = 12; // GCM IV length in bytes

    private String secretKey;

    // Method to generate the secret key
    public void generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE);
        SecretKey key = keyGenerator.generateKey();
        secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Encrypt method
    public String encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        SecretKey key = getSecretKeyFromBase64(secretKey);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

        // Combine IV and encrypted data
        byte[] combined = new byte[IV_LENGTH_BYTE + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, IV_LENGTH_BYTE);
        System.arraycopy(encryptedBytes, 0, combined, IV_LENGTH_BYTE, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    // Decrypt method
    public String decrypt(String ciphertext) throws Exception {
        byte[] decodedCombined = Base64.getDecoder().decode(ciphertext);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        byte[] encryptedBytes = new byte[decodedCombined.length - IV_LENGTH_BYTE];

        System.arraycopy(decodedCombined, 0, iv, 0, IV_LENGTH_BYTE);
        System.arraycopy(decodedCombined, IV_LENGTH_BYTE, encryptedBytes, 0, encryptedBytes.length);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        SecretKey key = getSecretKeyFromBase64(secretKey);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }

    // Helper method to get SecretKey from Base64 string
    private SecretKey getSecretKeyFromBase64(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }
}
