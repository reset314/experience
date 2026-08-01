package com.example.experience.infrastructure.log;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;

class MdcLoggingFilterTest {

    private final MdcLoggingFilter filter = new MdcLoggingFilter();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        MDC.clear();
    }

    private Map<String, String> captureMdc(MockHttpServletRequest request) throws Exception {
        Map<String, String> captured = new HashMap<>();
        FilterChain chain = (req, res) -> captured.putAll(MDC.getCopyOfContextMap());
        filter.doFilter(request, new MockHttpServletResponse(), chain);
        return captured;
    }

    @Test
    void shouldTakeFirstIpFromXForwardedFor() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/events");
        request.addHeader("X-Forwarded-For", "1.2.3.4, 10.0.0.1");
        request.setRemoteAddr("192.168.1.1");

        Map<String, String> mdc = captureMdc(request);

        assertThat(mdc.get(LogConstants.MdcKeys.CLIENT_IP)).isEqualTo("1.2.3.4");
    }

    @Test
    void shouldSkipUnknownInXForwardedFor() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/events");
        request.addHeader("X-Forwarded-For", "unknown, 6.7.8.9");
        request.setRemoteAddr("192.168.1.1");

        Map<String, String> mdc = captureMdc(request);

        assertThat(mdc.get(LogConstants.MdcKeys.CLIENT_IP)).isEqualTo("6.7.8.9");
    }

    @Test
    void shouldFallbackToXRealIp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/events");
        request.addHeader("X-Real-IP", "5.6.7.8");
        request.setRemoteAddr("192.168.1.1");

        Map<String, String> mdc = captureMdc(request);

        assertThat(mdc.get(LogConstants.MdcKeys.CLIENT_IP)).isEqualTo("5.6.7.8");
    }

    @Test
    void shouldFallbackToRemoteAddr() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/events");
        request.setRemoteAddr("192.168.1.1");

        Map<String, String> mdc = captureMdc(request);

        assertThat(mdc.get(LogConstants.MdcKeys.CLIENT_IP)).isEqualTo("192.168.1.1");
    }

    @Test
    void shouldWriteMethodAndPathToRequestUri() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/events");
        request.setRemoteAddr("192.168.1.1");

        Map<String, String> mdc = captureMdc(request);

        assertThat(mdc.get(LogConstants.MdcKeys.REQUEST_URI)).isEqualTo("POST /api/events");
    }

    @Test
    void shouldPutUserIdFromSecurityContext() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("user-123", null));

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/events");
        request.setRemoteAddr("192.168.1.1");

        Map<String, String> mdc = captureMdc(request);

        assertThat(mdc.get(LogConstants.MdcKeys.USER_ID)).isEqualTo("user-123");
    }

    @Test
    void shouldNotPutAnonymousUserId() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("anonymousUser", null));

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/events");
        request.setRemoteAddr("192.168.1.1");

        Map<String, String> mdc = captureMdc(request);

        assertThat(mdc.get(LogConstants.MdcKeys.USER_ID)).isNull();
    }

    @Test
    void shouldGenerateTraceIdAndClearMdcAfterwards() throws Exception {
        AtomicReference<String> traceIdDuringChain = new AtomicReference<>();
        FilterChain capturingChain = (req, res) -> traceIdDuringChain.set(MDC.get(LogConstants.MdcKeys.TRACE_ID));

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/events");
        request.setRemoteAddr("192.168.1.1");

        filter.doFilter(request, new MockHttpServletResponse(), capturingChain);

        assertThat(traceIdDuringChain.get()).isNotBlank();
        assertThat(MDC.get(LogConstants.MdcKeys.TRACE_ID)).isNull();
    }
}
