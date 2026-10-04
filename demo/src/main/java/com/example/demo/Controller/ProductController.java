package com.example.demo.Controller;

import com.example.demo.Dto.ProductResponseDto;
import com.example.demo.Service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductResponseDto> listPublic() {
        return service.listPublic();
    }

    @GetMapping("/{slug}")
    public ProductResponseDto getBySlug(@PathVariable String slug) {
        return service.getPublicBySlug(slug);
    }
}