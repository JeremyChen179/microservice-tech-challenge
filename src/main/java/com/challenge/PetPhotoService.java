package com.challenge;

import org.springframework.core.io.ClassPathResource;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.FileCopyUtils;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service 
public class PetPhotoService {
    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    // Candidate URLs based on your requirements and public-apis animal recommendations
    private final List<String> candidateUrls = List.of(
        "https://place.dog/300/200",
        "https://placebear.com/400/400",
        "https://picsum.photos/400/400" // Fallback alternative from public-apis ecosystem
    );
    private final String SCHEMA_FILE = "schema.sql";
    private final String UPDATE_SQL = "INSERT INTO fetched_photos (image_binary, timestamp, source_website) VALUES (?, ?, ?)";
    private final String SELECT_SQL = "SELECT timestamp, source_website, image_binary FROM fetched_photos ORDER BY id DESC LIMIT 1";

    /**
     * ctor
     * @param jdbcTemplate
     */
    public PetPhotoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @PostConstruct
    public void initDatabase() {
        try {
            // Load schema.sql from src/main/resources/schema.sql
            ClassPathResource resource = new ClassPathResource(SCHEMA_FILE);
            
            // Read the file contents into a String
            String sqlScript = FileCopyUtils.copyToString(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );

            // Execute the script against SQLite
            jdbcTemplate.execute(sqlScript);
            System.out.println("Successfully executed schema.sql on startup.");

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database from schema.sql", e);
        }
    }

    public String fetchAndSaveRandomPhoto() {
        byte[] imageBytes = null;
        String successfulSource = "";

        // Try downloading with error handling across options
        while (true) {
            String url = pickRandomUrl();
            try {
                imageBytes = restTemplate.getForObject(url, byte[].class);
                if (imageBytes != null && imageBytes.length > 0) {
                    successfulSource = url;
                    break;
                }
                System.err.println("url ok, but the fetched image is empty : 0 bytes");
            } catch (Exception e) {
                // Log and continue to the next website fallback
                System.err.println("Failed to fetch from " + url + ", trying next...");
            }
        }
        
        String timestamp = Instant.now().toString();

        // Save to SQLite embed database
        jdbcTemplate.update(UPDATE_SQL, imageBytes, timestamp, successfulSource);

        return successfulSource;
    }

    private String pickRandomUrl() {
        Random random = new Random();
        return candidateUrls.get(random.nextInt(candidateUrls.size()));
    }

    public Map<String, Object> getLatestPhoto() {
        try {
            return jdbcTemplate.queryForObject(SELECT_SQL, (rs, rowNum) -> Map.of(
                "timestamp", rs.getString("timestamp"),
                "sourceWebsite", rs.getString("source_website"),
                "imageBinary", rs.getBytes("image_binary")
            ));
        } catch (Exception e) {
             // Table might be empty initially
            return null;
        }
    }
}