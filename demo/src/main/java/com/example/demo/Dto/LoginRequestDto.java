package com.example.demo.Dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "Enter your email") String email,
        @NotBlank(message = "Enter your password") String password
) {}