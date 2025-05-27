package com.shield.shieldsleepapi.service;

import com.shield.shieldsleepapi.model.SleepData;
import com.shield.shieldsleepapi.model.SleepScoreResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SleepService {

    /**
     * Calculates the SHIELD Sleep Score and biological age delta based on provided sleep data.
     *
     * @param sleepData An object containing total sleep hours, sleep efficiency, REM percentage, age, and sex.
     * @return A SleepScoreResponse object containing the calculated score, bio-age delta, alerts, and suggestions.
     */
    public SleepScoreResponse calculateSleepScore(SleepData sleepData) {
        // Initialize base score to 100
        int shieldScore = 100;
        List<String> alerts = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        // Apply scoring rules based on the assignment specifications

        // Rule 1: IF total_sleep_hours < 6 THEN deduct 10 points
        if (sleepData.getTotalSleepHours() < 6) {
            shieldScore -= 10;
            alerts.add("Insufficient total sleep hours");
            suggestions.add("Aim for 7-9 hours of sleep per night for optimal health.");
        }

        // Rule 2: IF sleep_efficiency < 85 THEN deduct 5 points
        if (sleepData.getSleepEfficiency() < 85) {
            shieldScore -= 5;
            alerts.add("Low sleep efficiency");
            suggestions.add("Improve sleep efficiency by maintaining a consistent sleep schedule and creating a conducive sleep environment.");
        }

        // Rule 3: IF REM_percentage < 15 THEN deduct 5 points
        if (sleepData.getRemPercentage() < 15) {
            shieldScore -= 5;
            alerts.add("Low REM sleep percentage");
            suggestions.add("To increase REM sleep, prioritize consistent sleep, reduce alcohol intake before bed, and manage stress.");
        }

        // Rule 4: IF age > 50 AND sleep_hours < 6 THEN deduct 5 more points
        // Note: This rule explicitly checks 'sleep_hours < 6' again, which aligns with the prompt.
        if (sleepData.getAge() > 50 && sleepData.getTotalSleepHours() < 6) {
            shieldScore -= 5;
            alerts.add("Age-related insufficient sleep");
            suggestions.add("Older adults may require slightly less sleep, but consistently less than 6 hours can still be detrimental. Consult a doctor if sleep issues persist.");
        }

        // Ensure shield_score does not go below 0
        shieldScore = Math.max(0, shieldScore);

        // Calculate bio_age_delta based on the final shieldScore
        String bioAgeDelta;
        if (shieldScore >= 90) {
            bioAgeDelta = "-1.0";
        } else if (shieldScore >= 80) {
            bioAgeDelta = "+0.5";
        } else if (shieldScore >= 70) {
            bioAgeDelta = "+1.5";
        } else {
            bioAgeDelta = "+2.5";
        }

        return SleepScoreResponse.builder()
                .shieldScore(shieldScore)
                .bioAgeDelta(bioAgeDelta)
                .alerts(alerts)
                .suggestions(suggestions)
                .build();
    }
}
