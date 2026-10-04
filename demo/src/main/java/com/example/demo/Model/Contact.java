package com.example.demo.Model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "contact_messages")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    @jakarta.validation.constraints.NotBlank(message = "Enter your name")
    private String name;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotBlank(message = "Enter your email")
    @jakarta.validation.constraints.Email(message = "That email does not look right")
    private String email;

    @Column(length = 160)
    private String company;

    @Column(name = "inquiry_type", nullable = false, length = 40)
    private String inquiryType;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    @Column(nullable = false, length = 20)
    private String status = "NEW";

    @Column(name = "source_ip", length = 45)
    private String sourceIp;

    @Transient // honeypot only — never persisted
    private String website;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }


    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getInquiryType() { return inquiryType; }
    public void setInquiryType(String inquiryType) { this.inquiryType = inquiryType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSourceIp() { return sourceIp; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }
    public Instant getCreatedAt() { return createdAt; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
}