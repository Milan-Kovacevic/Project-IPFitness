package dev.projectfitness.ipfitness.models.dtos;

import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import lombok.Data;

import java.util.List;

@Data
public class FitnessProgram {
    private Integer programId;
    private String name;
    private String categoryName;
    private TrainingLocation location;
    private ProgramDifficulty difficulty;
    private String createdBy;
    private Integer userId;
    private Double price;
    private List<String> images;
}
