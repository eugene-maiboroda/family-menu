package com.familymenu.backend.adapter.shared;

import com.familymenu.backend.port.out.IdGenerationPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GenerateIdAdapter implements IdGenerationPort {

    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
