package com.familymenu.backend.shared.web;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public record ErrorDetailsResponse(
        String message,
        String exceptionName,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) Map<String, String> errors
) {
    public ErrorDetailsResponse(String message, String exceptionName) {
        this(message, exceptionName, Map.of());
    }
}
