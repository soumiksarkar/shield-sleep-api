package com.shield.shieldsleepapi.service;

import com.shield.shieldsleepapi.model.SleepData;
import com.shield.shieldsleepapi.model.SleepScoreResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SleepService {

    /**
     * Calculates the SHIELD Sleep Score and Bio-Age Delta based on provided sleep data.
     * This method contains hardcoded rules for demonstration purposes.
     * In a real-world scenario, this logic would likely be powered by a machine learning model
     * that learns dynamic weights and relationships from a large dataset.
     *
     * @param sleepData The sleep data provided by the user.
     * @return A SleepScoreResponse containing the score, bio-age delta, alerts, and suggestions.
     */
    public SleepScoreResponse calculateShieldScore(SleepData sleepData) {
        int score = 100; // Start with a perfect score
        List<String> alerts = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        // --- Rule-based Scoring and Alerts (Simulated ML Integration) ---
        // This is a simplified rule engine. An ML model would learn these complex relationships
        // and adjust 'weights' (feature importance) dynamically based on actual health outcomes.

        // 1. Total Sleep Hours
        // Recommended sleep hours: 7-9 hours for adults
        if (sleepData.getTotalSleepHours() < 6) {
            score -= 15;
            alerts.add("Insufficient total sleep hours.");
            suggestions.add("Aim for 7-9 hours of sleep per night for optimal health. Establish a consistent bedtime and wake-up time.");
            if (sleepData.getAge() >= 65 && sleepData.getTotalSleepHours() < 5) { // More severe for older adults
                score -= 10;
                alerts.add("Critically low sleep for older adult.");
                suggestions.add("For older adults, consistently less than 6 hours can be detrimental. Consider consulting a doctor if sleep issues persist.");
            }
        } else if (sleepData.getTotalSleepHours() > 9.5) {
            score -= 5; // Slight penalty for excessive sleep, could indicate underlying issues
            alerts.add("Excessive total sleep hours.");
            suggestions.add("Consistently sleeping too much might indicate underlying health issues or poor sleep quality. Review your sleep habits or consult a professional.");
        }

        // 2. Sleep Efficiency
        // Ideal: >= 85%
        if (sleepData.getSleepEfficiency() < 75) {
            score -= 20;
            alerts.add("Very low sleep efficiency.");
            suggestions.add("Focus on improving your sleep efficiency by limiting time awake in bed. Only go to bed when sleepy, and get out of bed if you can't sleep after 20 minutes.");
        } else if (sleepData.getSleepEfficiency() < 85) {
            score -= 10;
            alerts.add("Low sleep efficiency.");
            suggestions.add("Improve sleep efficiency by maintaining a consistent sleep schedule, avoiding stimulants before bed, and creating a conducive sleep environment.");
        }

        // 3. REM Percentage
        // Ideal: 20-25% of total sleep
        // For 7 hours sleep (420 min), 20% REM is 84 min, 25% is 105 min.
        double expectedRemMin = sleepData.getTotalSleepHours() * 60 * 0.20; // 20% of total sleep in minutes
        double actualRemMin = sleepData.getTotalSleepHours() * 60 * (sleepData.getRemPercentage() / 100.0);

        if (sleepData.getRemPercentage() < 15) {
            score -= 15;
            alerts.add("Low REM sleep percentage.");
            suggestions.add("To increase REM sleep, prioritize consistent sleep, reduce alcohol intake before bed, and manage stress through relaxation techniques like meditation.");
        } else if (sleepData.getRemPercentage() > 30) { // Can indicate issues if too high
            score -= 5;
            alerts.add("High REM sleep percentage.");
            suggestions.add("While not always negative, unusually high REM might be related to sleep disorders or certain medications. Monitor your sleep patterns and consider professional advice.");
        }

        // 4. Age and Sex Specific Adjustments
        // Age: Sleep needs change with age.
        if (sleepData.getAge() < 18) { // Assuming adult context, so penalize if too young but still provide score
            alerts.add("Age out of typical adult range. Sleep needs for youth differ.");
            suggestions.add("This score is optimized for adults (18+). Younger individuals have higher sleep needs.");
        } else if (sleepData.getAge() >= 65) {
            if (sleepData.getTotalSleepHours() > 8) { // Older adults might need slightly less sleep
                score -= 5; // Slight penalty if too much sleep for older adults
                alerts.add("Older adult, potentially excessive sleep.");
                suggestions.add("While sleep quality is key, consistently high sleep duration in older adults can sometimes indicate underlying issues. Discuss with your doctor if concerned.");
            }
        }

        // Sex: Some subtle differences, though less impactful than other factors for overall score
        // (No direct score deduction for sex, but could be used for personalized suggestions or ML features)
        if ("female".equalsIgnoreCase(sleepData.getSex()) && sleepData.getTotalSleepHours() < 7) {
            suggestions.add("Women might experience hormonal influences on sleep; maintaining consistent sleep hygiene is especially important.");
        }

        // --- Bio-Age Delta Calculation ---
        String bioAgeDelta;
        if (score >= 90) {
            bioAgeDelta = String.format("%.1f", -Math.abs(100 - score) / 10.0); // Up to -1 year for excellent sleep
        } else if (score >= 80) {
            bioAgeDelta = String.format("%.1f", -(Math.random() * 0.5)); // Slightly younger or same
        } else if (score >= 60) {
            bioAgeDelta = String.format("%.1f", (100 - score) / 10.0); // 1-4 years older
        } else {
            bioAgeDelta = String.format("%.1f", (100 - score) / 5.0); // Significantly older
        }

        // Ensure score doesn't go below 0
        score = Math.max(0, score);

        return new SleepScoreResponse(score, bioAgeDelta, alerts, suggestions);
    }
}
