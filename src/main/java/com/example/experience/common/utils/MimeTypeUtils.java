package com.example.experience.common.utils;
import java.util.HashMap;
import java.util.Map;

public class MimeTypeUtils {
    private static final Map<String, String> EXTENSION_MAP = new HashMap<>();
    static {
        EXTENSION_MAP.put("txt", "text/plain");
        EXTENSION_MAP.put("html", "text/html");
        EXTENSION_MAP.put("css", "text/css");
        EXTENSION_MAP.put("js", "application/javascript");
        EXTENSION_MAP.put("json", "application/json");
        EXTENSION_MAP.put("xml", "application/xml");
        EXTENSION_MAP.put("pdf", "application/pdf");
        EXTENSION_MAP.put("zip", "application/zip");
        EXTENSION_MAP.put("png", "image/png");
        EXTENSION_MAP.put("jpg", "image/jpeg");
        EXTENSION_MAP.put("jpeg", "image/jpeg");
        EXTENSION_MAP.put("gif", "image/gif");
        EXTENSION_MAP.put("bmp", "image/bmp");
        EXTENSION_MAP.put("svg", "image/svg+xml");
        EXTENSION_MAP.put("mp4", "video/mp4");
        EXTENSION_MAP.put("mp3", "audio/mpeg");
        EXTENSION_MAP.put("wav", "audio/wav");
        EXTENSION_MAP.put("", "application/octet-stream");
        EXTENSION_MAP.put("webp","image/webp");
        EXTENSION_MAP.put("c", "text/x-c");

    }

    public static String GuessContentType(String objectKey) {
        int lastDot = objectKey.lastIndexOf('.');
        if (lastDot != -1) {
            String ext = objectKey.substring(lastDot + 1).toLowerCase();
            String type = EXTENSION_MAP.get(ext);
            if (type != null) {
                return type;
            }
        }
        return "application/octet-stream";
    }
}