package com.example.experience.domain.rbac;

import com.example.experience.domain.rbac.entity.UserRole;
import com.example.experience.domain.user.entity.User;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRoleMappingTest {

    @Test
    void userRoleUsesCompositeKeyOfUserIdAndRole() {
        try (var registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .build()) {
            var metadata = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserRole.class)
                .buildMetadata();

            PersistentClass mapping = metadata.getEntityBinding(UserRole.class.getName());
            Property id = mapping.getIdentifierProperty();

            assertThat(id.isComposite()).isTrue();
            assertThat(((org.hibernate.mapping.Component) id.getValue()).getPropertyNames())
                .containsExactlyInAnyOrder("userId", "role");
            assertThat(id.getColumns()).extracting(c -> ((Column) c).getName())
                .containsExactlyInAnyOrder("user_id", "role");
            assertThat(mapping.getProperties()).extracting(Property::getName)
                .contains("user", "role");
        }
    }
}
