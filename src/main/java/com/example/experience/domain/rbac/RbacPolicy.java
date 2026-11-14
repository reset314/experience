package com.example.experience.domain.rbac;

import java.util.*;

public final class RbacPolicy {

    private static final Set<Permission> ADMIN = Collections.unmodifiableSet(EnumSet.of(
        Permission.USER_READ,
        Permission.USER_WRITE,
        Permission.DATASOURCE_READ,
        Permission.DATASOURCE_WRITE,
        Permission.DATASOURCE_MANAGE,
        Permission.EVENT_READ,
        Permission.EVENT_WRITE,
        Permission.SYNC_TRIGGER,
        Permission.SYNC_MANAGE,
        Permission.PLATFORM_READ,
        Permission.PLATFORM_WRITE,
        Permission.PLATFORM_MANAGE
    ));

    private static final Set<Permission> USER = Collections.unmodifiableSet(EnumSet.of(
        Permission.DATASOURCE_READ,
        Permission.DATASOURCE_WRITE,
        Permission.EVENT_READ,
        Permission.SYNC_TRIGGER
    ));

    private static final Set<Permission> AUDITOR = Collections.unmodifiableSet(EnumSet.of(
        Permission.USER_READ,
        Permission.DATASOURCE_READ,
        Permission.EVENT_READ,
        Permission.PLATFORM_READ
    ));

    private RbacPolicy() {}

    public static Set<Permission> permissionsOf(Role role) {
        return switch (role) {
            case SUPERADMIN -> EnumSet.allOf(Permission.class);
            case ADMIN -> ADMIN;
            case USER -> USER;
            case AUDITOR -> AUDITOR;
        };
    }

    public static Set<Permission> permissionsOf(Collection<Role> roles) {
        if (roles.contains(Role.SUPERADMIN)) {
            return EnumSet.allOf(Permission.class);
        }
        Set<Permission> permissions = EnumSet.noneOf(Permission.class);
        for (Role role : roles) {
            permissions.addAll(permissionsOf(role));
        }
        return permissions;
    }

    public static Set<String> authorityCodes(Collection<Role> roles) {
        Set<String> codes = new LinkedHashSet<>();
        for (Permission permission : permissionsOf(roles)) {
            codes.add(permission.getCode());
        }
        return codes;
    }
}
