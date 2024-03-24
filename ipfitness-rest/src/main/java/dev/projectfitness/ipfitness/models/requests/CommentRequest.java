package dev.projectfitness.ipfitness.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull
    private Integer userId;
    @NotBlank
    private String content;
}
