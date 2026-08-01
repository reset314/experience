package com.example.experience.infrastructure.log;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MdcLoggingFilter extends OncePerRequestFilter {

    private static final String UNKNOWN_IP = "unknown";
    private static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            MdcHelper.generateTraceId();
            MdcHelper.put(LogConstants.MdcKeys.REQUEST_URI,
                request.getMethod() + " " + request.getRequestURI());
            MdcHelper.put(LogConstants.MdcKeys.CLIENT_IP, getClientIp(request));

            String userId = getUserId();
            if (userId != null) {
                MdcHelper.put(LogConstants.MdcKeys.USER_ID, userId);
            }

            filterChain.doFilter(request, response);
        } finally {
            MdcHelper.clear();
        }
    }

    private String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof String principal)) {
            return null;
        }
        if (ANONYMOUS_USER.equals(principal) || !StringUtils.hasText(principal)) {
            return null;
        }
        return principal;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor)) {
            for (String ip : xForwardedFor.split(",")) {
                String trimmed = ip.trim();
                if (StringUtils.hasText(trimmed) && !UNKNOWN_IP.equalsIgnoreCase(trimmed)) {
                    return trimmed;
                }
            }
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xRealIp)) {
            return xRealIp.trim();
        }
        return request.getRemoteAddr();
    }
}
