package com.example.chatroom.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test {

    @GetMapping("hello")
    public String hello(){
        return "hello";
    }
}
