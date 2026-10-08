package com.familymenu.backend.bootstrap;

import com.familymenu.backend.bootstrap.props.ServerProps;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private static final String URL_TEMPLATE = "http://%s:%d%s/swagger-ui/index.html";
    private final ServerProps serverProps;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Swagger UI available at {}", String.format(
                URL_TEMPLATE,
                serverProps.host(),
                serverProps.port(),
                serverProps.servlet().contextPath()
        ));
    }
}
