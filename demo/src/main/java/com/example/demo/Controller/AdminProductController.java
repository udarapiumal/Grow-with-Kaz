package com.example.demo.Controller;

import com.example.demo.Dto.AdminProductResponseDto;
import com.example.demo.Dto.ProductFormDto;
import com.example.demo.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService service;

    public AdminProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<AdminProductResponseDto> list() {
        return service.listAdmin();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AdminProductResponseDto create(@Valid @ModelAttribute ProductFormDto form,
                                          @RequestParam(value = "image", required = false) MultipartFile image) {
        return service.create(form, image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdminProductResponseDto update(@PathVariable Long id,
                                          @Valid @ModelAttribute ProductFormDto form,
                                          @RequestParam(value = "image", required = false) MultipartFile image) {
        return service.update(id, form, image);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}