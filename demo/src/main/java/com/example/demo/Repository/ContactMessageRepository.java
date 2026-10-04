package com.example.demo.Repository;

import com.example.demo.Model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;

public interface ContactMessageRepository extends JpaRepository<Contact, Long> {

    // Simple spam guard: how many messages has this email sent recently?
    long countByEmailIgnoreCaseAndCreatedAtAfter(String email, Instant since);
}