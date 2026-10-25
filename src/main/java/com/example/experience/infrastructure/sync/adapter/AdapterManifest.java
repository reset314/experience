package com.example.experience.infrastructure.sync.adapter;

import java.util.List;
import java.util.Set;

public record AdapterManifest(
    int manifestVersion,
    String adapterKey,
    String displayName,
    String version,
    String description,
    Set<Capability> abilities,
    Set<FactoryService> factoryServices,
    String refershTokenable,
    String platformName,//与用于init时注册到数据库与platform.name和adapter.adapter_key
    String platformDisplayName,
    List<EventSpec> events,
    List<EntitySpec> entities
) {}