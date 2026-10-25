package com.example.experience.infrastructure.sync.adapter;

import java.util.List;

public record AuthRequest(
    int round,
    List<AuthFieldRecord> authfields
) {

}
