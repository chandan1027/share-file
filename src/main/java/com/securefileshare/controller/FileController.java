package com.securefileshare.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            String decodedFilename =
                    URLDecoder.decode(filename, StandardCharsets.UTF_8);

            Path filePath =
                    Paths.get(uploadDir).resolve(decodedFilename).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
