package com.example.chatroom.config.security.encryption;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Data
@Component
public class RSAEncryption {

    private static final String RSA_ALGORITHM = "RSA";
    private static final int RSA_KEY_SIZE = 2048;

    private String publicKey;
    private String privateKey;

    // Method to generate RSA keys
    public void generateKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(RSA_KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Encode the keys to Base64
        publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    // Encrypt method
    public String encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        PublicKey pubKey = getPublicKeyFromBase64(publicKey);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt method
    public String decrypt(String ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        PrivateKey privKey = getPrivateKeyFromBase64(privateKey);
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] decodedBytes = Base64.getDecoder().decode(ciphertext);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    // Helper method to get PublicKey from Base64 string
    private PublicKey getPublicKeyFromBase64(String base64Key) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePublic(spec);
    }

    // Helper method to get PrivateKey from Base64 string
    private PrivateKey getPrivateKeyFromBase64(String base64Key) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePrivate(spec);
    }
}
