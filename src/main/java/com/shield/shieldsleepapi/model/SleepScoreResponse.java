package com.shield.shieldsleepapi.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SleepScoreResponse {
    private int shieldScore;
    private String bioAgeDelta;
    private List<String> alerts;
    private List<String> suggestions;
}