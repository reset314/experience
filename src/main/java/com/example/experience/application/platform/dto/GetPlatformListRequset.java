package com.example.experience.application.platform.dto;

import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GetPlatformListRequset(
    String UserId,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant occurredAfter,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant occurredBefore,
    String q,

    @Min(0) Integer page,
    @Min(1) @Max(100) Integer size,
    String sortBy,
    String sortDirection
) {

}
