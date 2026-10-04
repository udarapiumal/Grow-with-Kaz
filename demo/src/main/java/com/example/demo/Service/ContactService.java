package com.example.demo.Service;

import com.example.demo.Model.Contact;
import com.example.demo.Repository.ContactMessageRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.Instant;

@Service
public class ContactService {

    private static final int MAX_PER_HOUR = 3;

    private final ContactMessageRepository repository;
    private final EmailService emailService;

    public ContactService(ContactMessageRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Transactional
    public Long submit(Contact request, String sourceIp) {
        // Honeypot tripped: act as if it worked so the bot stops retrying.
        if (request.getWebsite() != null && !request.getWebsite().isBlank()) {
            return -1L;
        }

        Instant hourAgo = Instant.now().minus(Duration.ofHours(1));
        long recent = repository.countByEmailIgnoreCaseAndCreatedAtAfter(request.getEmail(), hourAgo);
        if (recent >= MAX_PER_HOUR) {
            throw new TooManyMessagesException();
        }

        Contact entity = new Contact();
        entity.setName(request.getName().trim());
        entity.setEmail(request.getEmail().trim().toLowerCase());
        entity.setCompany(request.getCompany() == null || request.getCompany().isBlank()
                ? null : request.getCompany().trim());
        entity.setInquiryType(request.getInquiryType());
        entity.setMessage(request.getMessage().trim());
        entity.setSourceIp(sourceIp);

        Contact saved = repository.save(entity);
        emailService.sendContactNotification(saved);
        return saved.getId();
    }

    public static class TooManyMessagesException extends RuntimeException {
        public TooManyMessagesException() {
            super("You have sent a few messages already. Give me a little time to reply.");
        }
    }
}