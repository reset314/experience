package com.example.experience.domain.rbac;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RbacPolicyTest {

    @Test
    void superadminExpandsToEveryPermission() {
        assertThat(RbacPolicy.authorityCodes(List.of(Role.SUPERADMIN)))
            .containsExactlyInAnyOrderElementsOf(
                java.util.Arrays.stream(Permission.values()).map(Permission::getCode).toList());
    }

    @Test
    void adminCannotDeleteOrManageUsers() {
        Set<String> codes = RbacPolicy.authorityCodes(List.of(Role.ADMIN));

        assertThat(codes).contains("user:read", "user:write", "event:write", "sync:manage");
        assertThat(codes).doesNotContain("user:delete", "user:manage", "setting:manage");
    }

    @Test
    void userHasOnlyOwnDataPermissions() {
        assertThat(RbacPolicy.authorityCodes(List.of(Role.USER)))
            .containsExactlyInAnyOrder(
                "datasource:read", "datasource:write", "event:read", "sync:trigger");
    }

    @Test
    void auditorIsReadOnly() {
        assertThat(RbacPolicy.authorityCodes(List.of(Role.AUDITOR)))
            .containsExactlyInAnyOrder(
                "user:read", "datasource:read", "event:read", "platform:read");
    }

    @Test
    void multipleRolesUnionPermissions() {
        assertThat(RbacPolicy.authorityCodes(List.of(Role.USER, Role.AUDITOR)))
            .containsExactlyInAnyOrder(
                "user:read", "datasource:read", "datasource:write", "event:read",
                "sync:trigger", "platform:read");
    }

    @Test
    void roleCodeRoundTrips() {
        for (Role role : Role.values()) {
            assertThat(Role.fromCode(role.getCode())).isEqualTo(role);
        }
    }
}
