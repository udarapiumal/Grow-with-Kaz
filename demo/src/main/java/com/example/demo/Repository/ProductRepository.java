package com.example.demo.Repository;

import com.example.demo.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrueOrderByCreatedAtDesc();
    List<Product> findAllByOrderByCreatedAtDesc();
    Optional<Product> findBySlugAndActiveTrue(String slug);
    boolean existsBySlug(String slug);
}