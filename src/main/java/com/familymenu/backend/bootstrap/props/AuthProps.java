package com.familymenu.backend.bootstrap.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.auth")
public record AuthProps(

        String username,

        String passwordHash
) {
}
