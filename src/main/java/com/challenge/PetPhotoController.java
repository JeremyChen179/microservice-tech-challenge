package com.challenge;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/photos")
public class PetPhotoController {
    private final PetPhotoService petPhotoService;

    public PetPhotoController(PetPhotoService petPhotoService) {
        this.petPhotoService = petPhotoService;
    }

    // A. Fetch a random photo from APIs, save to SQLite, and return source metadata
    @PostMapping("/fetch")
    public ResponseEntity<Map<String, String>> fetchAndSave() {
        try {
            String source = petPhotoService.fetchAndSaveRandomPhoto();
            return ResponseEntity.ok(Map.of("status", "success", "source", source));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // B. Fetch the latest saved image binary directly from SQLite table for browser rendering
    @GetMapping(value = "/latest/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getLatestImageBinary() {
        Map<String, Object> latest = petPhotoService.getLatestPhoto();
        if (latest == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] imageBytes = (byte[]) latest.get("imageBinary");
        return ResponseEntity.ok(imageBytes);
    }

    @GetMapping("/latest/meta")
    public ResponseEntity<Map<String, Object>> getLatestMeta() {
        Map<String, Object> latest = petPhotoService.getLatestPhoto();
        if (latest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of(
            "timestamp", latest.get("timestamp"),
            "sourceWebsite", latest.get("sourceWebsite")
        ));
    }
}
