package com.example.experience.infrastructure.log;

import java.util.Map;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public final class MdcHelper {

    private MdcHelper() {}

    // 生成唯一的 traceId，并放入 MDC 上下文中
    public static String generateTraceId() {
        String existingTraceId = MDC.get(LogConstants.MdcKeys.TRACE_ID);
        if (StringUtils.hasText(existingTraceId)) {
            return existingTraceId;
        }
        String newTraceId = UUID.randomUUID().toString().replace("-", "");
        MDC.put(LogConstants.MdcKeys.TRACE_ID, newTraceId);
        return newTraceId;
    }

    // 将指定的键值对放入 MDC 上下文中
    public static void put(String key, String value) {
        if (StringUtils.hasText(key) && value != null) {
            MDC.put(key, value);
        }
    }

    // 将所有的键值对放入 MDC 上下文中
    public static void putAll(Map<String, String> contextMap) {
        if (contextMap == null || contextMap.isEmpty()) {
            return;
        }
        contextMap.forEach((key, value) -> {
            if (StringUtils.hasText(key) && value != null) {
                MDC.put(key, value);
            }
        });
    }

    // 从 MDC 上下文中获取指定键的值
    public static String get(String key) {
        return MDC.get(key);
    }

    // 获取 MDC 上下文的副本
    public static Map<String, String> getCopyOfContextMap() {
        return MDC.getCopyOfContextMap();
    }

    // 将指定的上下文映射设置到 MDC 中，覆盖现有的上下文
    public static void setContextMap(Map<String, String> contextMap) {
        if (contextMap != null && !contextMap.isEmpty()) {
            MDC.setContextMap(contextMap);
        }
    }

    // 清除当前MDC上下文中的所有键值对
    public static void clear() {
        MDC.clear();
    }

    // 从 MDC 上下文中移除指定的键
    public static void remove(String key) {
        MDC.remove(key);
    }
}