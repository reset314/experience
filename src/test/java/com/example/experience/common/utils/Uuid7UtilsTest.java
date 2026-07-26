package com.example.experience.common.utils;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class Uuid7UtilsTest {

    @Test
    void shouldGenerateUuidWithoutHyphens() {
        String uuid = Uuid7Utils.generateUuid7();
        assertThat(uuid).isNotNull();
        assertThat(uuid).hasSize(32);
        assertThat(uuid).doesNotContain("-");
    }

    @Test
    void shouldAddHyphensBack() {
        String uuid = Uuid7Utils.generateUuid7();
        String withHyphens = Uuid7Utils.getUuidWithHyphens(uuid);
        assertThat(withHyphens).hasSize(36);
        assertThat(withHyphens).contains("-");
    }
}