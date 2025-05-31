package com.shield.shieldsleepapi.controller;

import com.shield.shieldsleepapi.model.SleepData;
import com.shield.shieldsleepapi.model.SleepScoreResponse;
import com.shield.shieldsleepapi.service.SleepService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/sleep")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class SleepController {

    private final SleepService sleepService;

    @Autowired
    public SleepController(SleepService sleepService) {
        this.sleepService = sleepService;
    }

    /**
     * REST API endpoint to calculate the SHIELD Sleep Score and biological age delta.
     *
     * This method handles POST requests to "/api/sleep/score".
     * The request body is expected to be a JSON object that maps to the SleepData DTO.
     *
     * @param sleepData The input sleep data received from the request body.
     * @return A ResponseEntity containing the SleepScoreResponse and an HTTP status.
     */
    @PostMapping("/score")
    public ResponseEntity<SleepScoreResponse> calculateSleepScore(@Valid @RequestBody SleepData sleepData) {
        log.info("Received sleep data for scoring: {}", sleepData);
        try {
            SleepScoreResponse response = sleepService.calculateShieldScore(sleepData);
            log.info("Calculated sleep score response: {}", response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Validation error for sleep data: {}", e.getMessage());
            // For validation errors, return 400 Bad Request
            return ResponseEntity.badRequest().body(new SleepScoreResponse(0, null, Collections.singletonList(e.getMessage()), Collections.emptyList()));
        } catch (Exception e) {
            log.error("An unexpected error occurred while calculating sleep score: {}", e.getMessage(), e);
            // For other unexpected errors, return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SleepScoreResponse(0, null, Collections.singletonList("An internal server error occurred."), Collections.emptyList()));
        }
    }

    private static ResponseEntity<SleepScoreResponse> validateRequest(SleepData sleepData) {
        if (sleepData.getTotalSleepHours() < 0 || sleepData.getSleepEfficiency() < 0 ||
                sleepData.getRemPercentage() < 0 || sleepData.getAge() <= 0 || sleepData.getSex() == null || sleepData.getSex().trim().isEmpty()) {
            log.warn("Invalid input data received: {}", sleepData);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    /**
     * Mock endpoint for lab report upload simulation.
     * In a real application, this would handle secure file storage,
     * OCR processing, and biomarker extraction.
     */
    @PostMapping("/lab/upload")
    public ResponseEntity<Map<String, String>> uploadLabReport(@RequestParam("file") MultipartFile file) {
        log.info("Received simulated lab report upload request for file: {}", file.getOriginalFilename());

        // In a real scenario:
        // 1. Securely store the file (e.g., encrypted S3 bucket).
        // 2. Queue for OCR processing.
        // 3. Extract biomarker data.
        // 4. Validate and store biomarker data securely.
        // 5. Link to patient record.

        // For simulation, just acknowledge receipt
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "No file selected for upload."));
        }

        String fileName = file.getOriginalFilename();
        // Simulate a delay for processing
        try {
            Thread.sleep(1000); // Simulate 1 second processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Mock upload sleep interrupted", e);
        }

        log.info("Simulated processing of lab report: {}", fileName);
        return ResponseEntity.ok(Map.of("message", "Lab report '" + fileName + "' received for simulated processing."));
    }
}
