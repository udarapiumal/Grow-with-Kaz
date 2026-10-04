package com.example.demo.Repository;

import com.example.demo.Model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByEmailIgnoreCase(String email);
}