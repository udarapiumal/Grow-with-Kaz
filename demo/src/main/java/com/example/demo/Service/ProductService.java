package com.example.demo.Service;


import com.example.demo.Dto.AdminProductResponseDto;
import com.example.demo.Dto.ProductFormDto;
import com.example.demo.Dto.ProductResponseDto;
import com.example.demo.Model.Product;
import com.example.demo.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Locale;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final FileStorageService storage;

    public ProductService(ProductRepository repository, FileStorageService storage) {
        this.repository = repository;
        this.storage = storage;
    }

    public List<ProductResponseDto> listPublic() {
        return repository.findByActiveTrueOrderByCreatedAtDesc()
                .stream().map(ProductMapper::toPublic).toList();
    }

    public ProductResponseDto getPublicBySlug(String slug) {
        Product product = repository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return ProductMapper.toPublic(product);
    }

    public List<AdminProductResponseDto> listAdmin() {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream().map(ProductMapper::toAdmin).toList();
    }

    @Transactional
    public AdminProductResponseDto create(ProductFormDto form, MultipartFile image) {
        Product product = new Product();
        applyForm(product, form);
        product.setSlug(uniqueSlug(form.getTitle()));
        if (image != null && !image.isEmpty()) {
            product.setImagePath(storage.save(image));
        }
        return ProductMapper.toAdmin(repository.save(product));
    }

    @Transactional
    public AdminProductResponseDto update(Long id, ProductFormDto form, MultipartFile image) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        // Only regenerate the slug if the title actually changed, so existing
        // links to /products/<slug> don't silently break on every edit.
        if (!form.getTitle().equalsIgnoreCase(product.getTitle())) {
            product.setSlug(uniqueSlug(form.getTitle()));
        }
        applyForm(product, form);

        if (image != null && !image.isEmpty()) {
            product.setImagePath(storage.save(image));
        }
        return ProductMapper.toAdmin(repository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        repository.deleteById(id);
    }

    private void applyForm(Product product, ProductFormDto form) {
        product.setTitle(form.getTitle().trim());
        product.setDescription(form.getDescription().trim());
        product.setCategory(form.getCategory().trim());
        product.setPriceCents(form.getPriceCents());
        product.setActive(form.isActive());
    }

    private String uniqueSlug(String title) {
        String base = title.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");
        if (base.isBlank()) base = "product";

        String candidate = base;
        int suffix = 2;
        while (repository.existsBySlug(candidate)) {
            candidate = base + "-" + suffix++;
        }
        return candidate;
    }
}