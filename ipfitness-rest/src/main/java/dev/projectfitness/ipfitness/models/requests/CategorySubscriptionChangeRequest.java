package dev.projectfitness.ipfitness.models.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategorySubscriptionChangeRequest {
    @NotNull
    private Integer categoryId;
    @NotNull
    private Boolean subscribe;
}
