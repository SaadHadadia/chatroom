package com.example.chatroom.controllers;

//import com.example.chatroom.config.security.authorisation.UserSecurity;
import com.example.chatroom.config.security.authorisation.UserSecurity;
import com.example.chatroom.services.PersonalAccessTokenService;
import com.example.chatroom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @Autowired
    protected PersonalAccessTokenService tokenService;

    @Autowired
    protected UserService userService;

    @GetMapping("test")
    public String hello(){
        UserSecurity user = new UserSecurity(userService);
        user.isEmailVerified();
        return "Hello, " + user.isEmailVerified();
    }
}
