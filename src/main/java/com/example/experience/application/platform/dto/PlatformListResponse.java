package com.example.experience.application.platform.dto;

import java.util.List;

public record PlatformListResponse(
    List<PlatformResponse> platforms,

    long total,
    int totalPages,
    int pageNumber,
    int page,
    int size,
    boolean first,
    boolean last,
    boolean empty
) {

}
