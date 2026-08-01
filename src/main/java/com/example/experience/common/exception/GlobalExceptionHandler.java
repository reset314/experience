package com.example.experience.common.exception;

import static net.logstash.logback.argument.StructuredArguments.kv;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.experience.infrastructure.log.LogConstants;
import com.example.experience.infrastructure.log.MdcHelper;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        MdcHelper.put(LogConstants.MdcKeys.EVENT_TYPE, LogConstants.EventTypes.RESOURCE_NOT_FOUND);
        try {
            log.warn("resource_not_found {} {} {} {}",
                    kv("resource", ex.getResourceName()),
                    kv("field", ex.getFieldName()),
                    kv("value", ex.getFieldValue()),
                    kv("desc", "资源未找到"),
                    ex);
            return buildResponse(ex, HttpStatus.NOT_FOUND, "Resource not found");
        } finally {
            MdcHelper.remove(LogConstants.MdcKeys.EVENT_TYPE);
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        MdcHelper.put(LogConstants.MdcKeys.EVENT_TYPE, LogConstants.EventTypes.ACCESS_DENIED);
        try {
            log.warn("access_denied {}", kv("desc", "无权限访问"), ex);
            return buildResponse(ex, HttpStatus.FORBIDDEN, "Access denied");
        } finally {
            MdcHelper.remove(LogConstants.MdcKeys.EVENT_TYPE);
        }
    }

    private ResponseEntity<ErrorResponse> buildResponse(Throwable ex, HttpStatus status, String defaultDetail) {
        ErrorResponse response = ErrorResponse.builder(ex, status, defaultDetail)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}