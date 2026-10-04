package com.example.demo.Configuration;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // JSON API with a same-site session cookie: no <form> posts to
                // protect against CSRF-wise, and the frontend never runs on a
                // different site than the API in this setup. Revisit if that changes.
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers("/api/contact").permitAll()
                        .requestMatchers("/api/admin/login", "/api/admin/logout").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/api/admin/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(401, "Sign in required")))
                .sessionManagement((SessionManagementConfigurer<HttpSecurity> sm) ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }
}