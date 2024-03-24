package dev.projectfitness.ipfitness.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

import static dev.projectfitness.ipfitness.util.Constants.DATE_TIME_FORMAT;

@Data
public class SingleFitnessProgram {
    private Integer programId;
    private String name;
    private String description;
    private Integer categoryId;
    private List<AttributeValue> attributeValues;
    private TrainingLocation location;
    private ProgramDifficulty difficulty;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private Date endDate;
    private Double price;
    private List<String> images;
    private List<String> demonstrations;
}
