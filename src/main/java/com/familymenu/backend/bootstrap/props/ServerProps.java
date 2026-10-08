package com.familymenu.backend.bootstrap.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server")
public record ServerProps(
        String host,
        int port,
        Servlet servlet
) {

    public record Servlet(
            String contextPath
    ) {
    }
}
