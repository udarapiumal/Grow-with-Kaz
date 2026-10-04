package com.example.demo.Dto;

public record ProductResponseDto(
        String slug, String title, String description, String category,
        Integer priceCents, String imageUrl
) {}