package com.example.chatroom.config.security.encryption;

import com.example.chatroom.models.Message;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class Encryptor {

    private final AESEncryption symEncryption;
    private final RSAEncryption asymEncryption;

    @Autowired
    public Encryptor(AESEncryption symEncryption, RSAEncryption asymEncryption) {
        this.symEncryption = symEncryption;
        this.asymEncryption = asymEncryption;
    }

    public Message encrypt(Message msg) throws Exception {
        this.symEncryption.generateSecretKey();
//        msg.setSecretKey(this.symEncryption.getSecretKey());
        msg.setContent(this.symEncryption.encrypt(msg.getContent()));
        return msg;
    }

//    public ChatMessage decrypt(ChatMessage msg) throws Exception {
//        msg.setSecretKey(getSymEncryption().getSecretKey());
//        msg.setContent(getSymEncryption().encrypt(msg.getContent()));
//        System.out.println(msg.toString());
//        return msg;
//    }

}
