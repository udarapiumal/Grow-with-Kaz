package com.example.demo.Service;

import com.example.demo.Dto.AdminProductResponseDto;
import com.example.demo.Dto.ProductResponseDto;
import com.example.demo.Model.Product;

public final class ProductMapper {

    private ProductMapper() {}

    private static String formatImageUrl(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return null;
        }
        return imagePath.startsWith("/") ? imagePath : "/" + imagePath;
    }

    public static ProductResponseDto toPublic(Product p) {
        return new ProductResponseDto(
                p.getSlug(),
                p.getTitle(),
                p.getDescription(),
                p.getCategory(),
                p.getPriceCents(),
                formatImageUrl(p.getImagePath())
        );
    }

    public static AdminProductResponseDto toAdmin(Product p) {
        return new AdminProductResponseDto(
                p.getId(),
                p.getSlug(),
                p.getTitle(),
                p.getDescription(),
                p.getCategory(),
                p.getPriceCents(),
                formatImageUrl(p.getImagePath()),
                p.isActive(),
                p.getCreatedAt()
        );
    }
}