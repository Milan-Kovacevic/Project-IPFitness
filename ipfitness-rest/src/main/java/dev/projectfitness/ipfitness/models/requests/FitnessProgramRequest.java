package dev.projectfitness.ipfitness.models.requests;

import lombok.Data;

@Data
public class FitnessProgramRequest {
    private Integer userId;
    private String name;
    // ...
}
