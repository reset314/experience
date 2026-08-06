package com.example.experience.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app.adapter")
@Getter
@Setter
public class AdapterProperties {

    private String basePath = System.getProperty("user.home") + "/.experience/adapters";
    private String builtinPackage = "com.example.experience.infrastructure.sync.adapter.builtin";
}
