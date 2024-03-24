package dev.projectfitness.ipfitness.models.requests;

import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class FitnessProgramAddRequest {
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
    @FutureOrPresent
    private Date endDate;
    @NotNull
    private Integer categoryId;
    private @Valid AttributeValueRequest[] attributeValues;
    private MultipartFile[] pictures;
    private List<String> demonstrations;
}
