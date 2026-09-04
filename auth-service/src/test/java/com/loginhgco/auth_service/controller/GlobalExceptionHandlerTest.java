package com.loginhgco.auth_service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void runtimeExceptionProducesBadRequestWithMessageAndTimestamp() {
        ResponseEntity<Map<String, Object>> response = handler.handleRuntimeException(new RuntimeException("invalid credentials"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("invalid credentials", response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }
}
