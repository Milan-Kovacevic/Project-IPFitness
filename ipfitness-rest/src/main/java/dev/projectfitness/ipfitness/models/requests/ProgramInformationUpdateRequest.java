package dev.projectfitness.ipfitness.models.requests;

import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class ProgramInformationUpdateRequest {
    @NotNull
    private Integer userId;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private TrainingLocation location;
    @NotNull
    private ProgramDifficulty difficulty;
    @NotNull
    @Min(0)
    @Max(1000)
    private Double price;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private Integer categoryId;
    private @Valid AttributeValueRequest[] values;
}
