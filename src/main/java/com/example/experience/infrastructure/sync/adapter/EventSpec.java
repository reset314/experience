package com.example.experience.infrastructure.sync.adapter;

import java.util.Set;

public record EventSpec(
        String eveString,
        String displayName,
        TypedPath identity,
        Set<String> ignoreFields,
        int schemaVersion
        ) {

}
