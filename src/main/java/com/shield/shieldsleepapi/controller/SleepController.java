package com.shield.shieldsleepapi.controller;

import com.shield.shieldsleepapi.model.SleepData;
import com.shield.shieldsleepapi.model.SleepScoreResponse;
import com.shield.shieldsleepapi.service.SleepService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<SleepScoreResponse> calculateSleepScore(
            @RequestBody SleepData sleepData
    ) {
        log.info("Received request to calculate sleep score for data: {}", sleepData);

        try {
            validateRequest(sleepData);
            SleepScoreResponse response = sleepService.calculateSleepScore(sleepData);
            log.info("Successfully calculated sleep score. Response: {}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("An unexpected error occurred while calculating sleep score for data: {}", sleepData, e);
            return new ResponseEntity<>(
                    SleepScoreResponse.builder()
                            .alerts(List.of("An internal server error occurred. Please try again later."))
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
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
}
