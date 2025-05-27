package com.shield.shieldsleepapi.model;

import lombok.Data;

@Data
public class SleepData {
    private double totalSleepHours;
    private double sleepEfficiency;
    private double remPercentage;
    private int age;
    private String sex;
}
