package com.example.experience.infrastructure.log;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.example.experience.infrastructure.log.LogConstants.EventTypes;

class LogConstantsTest {

    @Test
    void eventTypesMatchAuditAndSyncDomain() {
        Set<String> expected = Set.of(
            "user.login", "user.logout", "role.change", "credential.access",
            "data.export", "data.delete", "sync.run", "sync.success", "sync.failed",
            "resource.not_found", "auth.failed", "access.denied"
        );

        Set<String> actual = Set.of(
            EventTypes.USER_LOGIN, EventTypes.USER_LOGOUT, EventTypes.ROLE_CHANGE,
            EventTypes.CREDENTIAL_ACCESS, EventTypes.DATA_EXPORT, EventTypes.DATA_DELETE,
            EventTypes.SYNC_RUN, EventTypes.SYNC_SUCCESS, EventTypes.SYNC_FAILED,
            EventTypes.RESOURCE_NOT_FOUND, EventTypes.AUTH_FAILED, EventTypes.ACCESS_DENIED
        );

        assertThat(actual).isEqualTo(expected);
    }
}