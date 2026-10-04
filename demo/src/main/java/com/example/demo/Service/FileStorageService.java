package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        );
        String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;

        try {
            Path targetLocation = this.uploadDir.resolve(uniqueFilename);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }
            return "uploads/" + uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + originalFilename, e);
        }
    }
}