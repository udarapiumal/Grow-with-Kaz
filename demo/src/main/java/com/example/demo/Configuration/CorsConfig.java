package com.example.demo.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Only needed if the browser calls port 8080 directly. With the Vite proxy the
 * browser sees a single origin and CORS never comes into play.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // Lets the browser send the admin session cookie on cross-origin
                // requests once frontend and backend are deployed separately.
                .allowCredentials(true);
    }
}