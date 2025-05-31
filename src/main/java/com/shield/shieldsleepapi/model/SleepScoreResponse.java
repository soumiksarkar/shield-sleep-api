package com.shield.shieldsleepapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SleepScoreResponse {
    private int shieldScore;
    private String bioAgeDelta; // Using String to include "+/-" sign
    private List<String> alerts;
    private List<String> suggestions;
}