package com.example.chatroom.config.security.encryption;

import jakarta.persistence.Column;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class ServerEncryption {

    private static ServerEncryption serverEncryption;
    protected String privatekey;
    protected String publickey;

    private ServerEncryption() throws Exception {
        RSAEncryption e = new RSAEncryption();
        e.generateKeys();
        privatekey = e.getPrivateKey();
        publickey = e.getPublicKey();
    }

    public static ServerEncryption getInstance() throws Exception {
        if (serverEncryption == null) {
            serverEncryption = new ServerEncryption();
        }
        return serverEncryption;
    }

    public void refresh() throws Exception {
        if (serverEncryption != null){
            RSAEncryption e = new RSAEncryption();
            e.generateKeys();
            privatekey = e.getPrivateKey();
            publickey = e.getPublicKey();
        }
    }

}
