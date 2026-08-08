package com.familymenu.backend.bootstrap;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "server")
public class AppProps {

    private String host;
    private int port;
    private Servlet servlet;

    @Getter
    @Setter
    public static class Servlet {
        private String contextPath;
    }
}

