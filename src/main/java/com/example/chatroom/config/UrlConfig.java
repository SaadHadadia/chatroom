package com.example.chatroom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class UrlConfig {

    private static Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) { UrlConfig.environment = environment; }

    public static String getUrl() {
        String baseUrl = environment.getProperty("base.url");
        String port = environment.getProperty("server.port");
        String contextPath = environment.getProperty("server.servlet.context-path");
        return baseUrl + ":" + port + contextPath;
    }

}
