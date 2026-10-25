package com.example.experience.domain.entity.entity;

public enum RelationType {
    PARENT_CHILD("parent_child"),
    SIBLING("sibling"),
    FRIEND("friend"),
    COLLEAGUE("colleague"),
    PARTNER("partner"),
    SPOUSE("spouse"),
    OTHER("other");

    private final String value;

    RelationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

