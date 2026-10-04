package com.example.demo.Controller;

import com.example.demo.Model.Contact;
import com.example.demo.Service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> submit(@Valid @RequestBody Contact request,
                                                      HttpServletRequest http) {
        service.submit(request, clientIp(http));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Thanks. Your message is in, I will reply by email."));
    }

    private String clientIp(HttpServletRequest http) {
        String forwarded = http.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return http.getRemoteAddr();
    }
}