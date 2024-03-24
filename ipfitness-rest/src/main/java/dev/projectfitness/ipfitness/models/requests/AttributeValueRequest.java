package dev.projectfitness.ipfitness.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttributeValueRequest {
    @NotNull
    private Integer attributeId;
    @NotBlank
    private String value;
}
