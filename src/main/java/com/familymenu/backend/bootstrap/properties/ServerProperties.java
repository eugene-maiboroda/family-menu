package com.familymenu.backend.bootstrap.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server")
public record ServerProperties(
        String host,
        int port,
        Servlet servlet
) {

    public record Servlet(
            String contextPath
    ) {
    }
}
