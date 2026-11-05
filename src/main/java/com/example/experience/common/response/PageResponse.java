package com.example.experience.common.response;

import java.util.List;

public record PageResponse<T>(
    List<T> items,
    long total,
    int page,
    int size,
    int totalPages
) {
    public static<T> PageResponse<T> empty() {
        return new PageResponse<T>(List.of(), 0, 0, 0, 0);
    }

}
