package com.example.experience.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

import com.example.experience.infrastructure.log.LogConstants;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldReturnNotFoundForMissingResource() {
        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFoundException(
            new ResourceNotFoundException("User", "123"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldLogResourceNotFoundWithEventType() {
        Logger logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        try {
            logger.addAppender(appender);

            handler.handleResourceNotFoundException(new ResourceNotFoundException("User", "123"));

            List<ILoggingEvent> events = appender.list;
            assertThat(events).hasSize(1);
            ILoggingEvent event = events.get(0);
            assertThat(event.getLevel()).isEqualTo(Level.WARN);
            assertThat(event.getFormattedMessage())
                .contains("resource_not_found")
                .contains("resource=User")
                .contains("field=id")
                .contains("value=123")
                .contains("desc=资源未找到");
            assertThat(event.getMDCPropertyMap())
                .containsEntry(LogConstants.MdcKeys.EVENT_TYPE, LogConstants.EventTypes.RESOURCE_NOT_FOUND);
        } finally {
            logger.detachAppender(appender);
        }
    }

    @Test
    void shouldReturnForbiddenForAccessDenied() {
        ResponseEntity<ErrorResponse> response = handler.handleAccessDeniedException(
            new AccessDeniedException("no permission"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldLogAccessDeniedWithEventType() {
        Logger logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        try {
            logger.addAppender(appender);

            handler.handleAccessDeniedException(new AccessDeniedException("no permission"));

            List<ILoggingEvent> events = appender.list;
            assertThat(events).hasSize(1);
            ILoggingEvent event = events.get(0);
            assertThat(event.getLevel()).isEqualTo(Level.WARN);
            assertThat(event.getFormattedMessage())
                .contains("access_denied")
                .contains("desc=无权限访问");
            assertThat(event.getMDCPropertyMap())
                .containsEntry(LogConstants.MdcKeys.EVENT_TYPE, LogConstants.EventTypes.ACCESS_DENIED);
        } finally {
            logger.detachAppender(appender);
        }
    }
}