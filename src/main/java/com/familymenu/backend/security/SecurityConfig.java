package com.familymenu.backend.security;

import com.familymenu.backend.bootstrap.props.AuthProps;
import com.familymenu.backend.shared.web.ErrorResponseWriter;
import org.springframework.http.HttpStatus;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AuthProps.class)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ErrorResponseWriter errorResponseWriter) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/auth/csrf").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) -> errorResponseWriter.writeError(
                                response, HttpStatus.UNAUTHORIZED, exception, "Authentication is required"))
                        .accessDeniedHandler((request, response, exception) -> errorResponseWriter.writeError(
                                response, HttpStatus.FORBIDDEN, exception, "Access is denied or CSRF token is invalid")))
                .formLogin(form -> form
                        .permitAll()
                        .failureHandler((request, response, exception) -> errorResponseWriter.writeError(
                                response, HttpStatus.UNAUTHORIZED, exception, "Invalid username or password")));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AuthProps properties) {
        String username = properties.username();
        String passwordHash = properties.passwordHash();

        if (username == null || username.isBlank()) {
            throw new IllegalStateException("Authentication username is required");
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalStateException("Authentication password hash is required");
        }

        UserDetails admin = User.withUsername(username)
                .password(passwordHash)
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
