package dev.projectfitness.ipfitness.models.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActivityAddRequest {
    @NotNull
    private Integer userId;
    @NotBlank
    private String activityType;
    @NotNull
    @Min(0)
    private Integer trainingDuration;
    @NotNull
    @Min(0)
    @Max(100)
    private Integer percentageCompleted;
    @NotNull
    @Min(0)
    private Double result;
    private String summary;
}
