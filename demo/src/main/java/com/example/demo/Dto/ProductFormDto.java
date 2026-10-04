package com.example.demo.Dto;

import jakarta.validation.constraints.*;

public class ProductFormDto {

    @NotBlank(message = "Enter a title") @Size(max = 160)
    private String title;

    @NotBlank(message = "Enter a description")
    private String description;

    @NotBlank(message = "Choose a category") @Size(max = 60)
    private String category;

    @NotNull(message = "Enter a price") @Min(value = 0, message = "Price can't be negative")
    private Integer priceCents;

    private boolean active = true;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getPriceCents() { return priceCents; }
    public void setPriceCents(Integer priceCents) { this.priceCents = priceCents; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}