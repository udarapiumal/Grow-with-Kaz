package com.example.demo.Controller;

import com.example.demo.Dto.LoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final AuthenticationManager authenticationManager;

    public AdminAuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request, HttpServletRequest httpRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // Persist the login into the session so it survives this one request.
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            return ResponseEntity.ok(Map.of("email", request.email()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Incorrect email or password."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(Authentication authentication) {
        // If this line runs at all, SecurityConfig already confirmed the
        // session is authenticated — an anonymous request never reaches here.
        return ResponseEntity.ok(Map.of("email", authentication.getName()));
    }
}