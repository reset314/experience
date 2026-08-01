package com.example.experience.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.example.experience.common.exception.AccessDeniedException;
import com.example.experience.common.exception.ResourceNotFoundException;

class AuthorizationServiceTest {

    private final AuthorizationService authorizationService = new AuthorizationService();

    private record TestResource(String id, String createdBy) {
    }

    @Test
    void shouldReturnResourceWhenOwnedByUser() {
        TestResource resource = new TestResource("r1", "u1");

        TestResource result = authorizationService.requireOwned(
            "u1", "r1", "TestResource",
            id -> Optional.of(resource),
            TestResource::createdBy);

        assertThat(result).isSameAs(resource);
    }

    @Test
    void shouldThrowNotFoundWhenResourceMissing() {
        assertThatThrownBy(() -> authorizationService.requireOwned(
                "u1", "missing", "TestResource",
                id -> Optional.empty(),
                TestResource::createdBy))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("TestResource not found with id : 'missing'");
    }

    @Test
    void shouldThrowAccessDeniedWhenOwnedByOtherUser() {
        TestResource resource = new TestResource("r1", "u2");

        assertThatThrownBy(() -> authorizationService.requireOwned(
                "u1", "r1", "TestResource",
                id -> Optional.of(resource),
                TestResource::createdBy))
            .isInstanceOf(AccessDeniedException.class)
            .hasMessageContaining("User u1 has no access to TestResource with id 'r1'");
    }

    @Test
    void shouldFindByActualIdInMap() {
        Map<String, TestResource> resources = Map.of(
            "r1", new TestResource("r1", "u1"));

        TestResource result = authorizationService.requireOwned(
            "u1", "r1", "TestResource",
            id -> Optional.ofNullable(resources.get(id)),
            TestResource::createdBy);

        assertThat(result).isEqualTo(resources.get("r1"));
    }
}
