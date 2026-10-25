package com.example.experience.infrastructure.sync.adapter;

import java.util.List;

public record AuthResult(
    AuthState state,
    String message,
    int round,
    boolean needInput,
    List<AuthFieldRecord> authFields
) {

}
