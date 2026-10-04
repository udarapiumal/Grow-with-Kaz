package com.example.demo.Dto;

import jakarta.validation.constraints.*;

/**
 * What the browser is allowed to send. There is no id or status here on
 * purpose: the client never gets to set those.
 */
public record ContactRequestDto(

        @NotBlank(message = "Enter your name")
        @Size(max = 120)
        String name,

        @NotBlank(message = "Enter your email")
        @Email(message = "That email does not look right")
        @Size(max = 255)
        String email,

        @Size(max = 160)
        String company,

        @NotBlank(message = "Choose an inquiry type")
        @Pattern(regexp = "JOB|FREELANCE|BRAND|SPEAKING|OTHER",
                message = "Choose an inquiry type")
        String inquiryType,

        @NotBlank(message = "Tell me a little about the project")
        @Size(min = 10, max = 5000, message = "Message must be at least 10 characters")
        String message,

        /** Hidden field. Real people leave it empty, most bots fill it in. */
        String website
) {}