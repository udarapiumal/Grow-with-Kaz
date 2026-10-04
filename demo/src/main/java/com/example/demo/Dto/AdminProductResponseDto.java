package com.example.demo.Dto;

import java.time.Instant;

public record AdminProductResponseDto(
        Long id, String slug, String title, String description, String category,
        Integer priceCents, String imageUrl, boolean active, Instant createdAt
) {}