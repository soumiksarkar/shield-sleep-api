package com.shield.shieldsleepapi.model;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString
public class SleepData {
    @NotNull(message = "Total sleep hours cannot be null.")
    @DecimalMin(value = "0.0", message = "Total sleep hours must be positive.")
    @DecimalMax(value = "24.0", message = "Total sleep hours cannot exceed 24.")
    private Double totalSleepHours;

    @NotNull(message = "Sleep efficiency cannot be null.")
    @DecimalMin(value = "0.0", message = "Sleep efficiency must be positive.")
    @DecimalMax(value = "100.0", message = "Sleep efficiency cannot exceed 100.")
    private Double sleepEfficiency;

    @NotNull(message = "REM percentage cannot be null.")
    @DecimalMin(value = "0.0", message = "REM percentage must be positive.")
    @DecimalMax(value = "100.0", message = "REM percentage cannot exceed 100.")
    private Double remPercentage;

    @NotNull(message = "Age cannot be null.")
    @Min(value = 1, message = "Age must be at least 1.")
    @Max(value = 120, message = "Age cannot exceed 120.")
    private Integer age;

    @NotBlank(message = "Sex cannot be blank.")
    private String sex;
}
