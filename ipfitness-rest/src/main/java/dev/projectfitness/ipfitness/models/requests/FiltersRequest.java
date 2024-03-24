package dev.projectfitness.ipfitness.models.requests;

import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.ProgramState;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltersRequest {
    private Integer categoryFilter;
    private TrainingLocation location;
    private ProgramDifficulty difficulty;
    private ProgramState state;
    private String searchQuery;
}
