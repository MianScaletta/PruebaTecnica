package com.calderon.pruebatecnica.exceptions;

import com.calderon.pruebatecnica.dto.ApiResponse;
import com.calderon.pruebatecnica.dto.Meta;
import com.calderon.pruebatecnica.dto.MensajeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        logger.error("Bad request: {}", ex.getMessage(), ex);
        Meta meta = new Meta("ERROR");
        Map<String, MensajeDTO> data = new HashMap<>();
        data.put("Mensaje", new MensajeDTO(ex.getMessage()));
        ApiResponse<Object> resp = new ApiResponse<>(meta, data);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAll(Exception ex) {
        logger.error("Unhandled exception", ex);
        Meta meta = new Meta("ERROR");
        Map<String, MensajeDTO> data = new HashMap<>();
        data.put("Mensaje", new MensajeDTO(ex.getMessage()));
        ApiResponse<Object> resp = new ApiResponse<>(meta, data);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }
}