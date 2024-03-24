package dev.projectfitness.ipfitness.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static dev.projectfitness.ipfitness.util.Constants.DATE_TIME_FORMAT;

@Data
public class FitnessProgramDetails {
    private Integer programId;
    private String name;
    private Double price;
    private TrainingLocation location;
    private ProgramDifficulty difficulty;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private Date endDate;
    private Integer numberOfParticipants;
    private Integer numberOfComments;
    private String description;
    private String categoryName;
    private List<ProgramAttribute> attributes;
    private List<String> pictures;
    private Integer ownerId;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerBiography;
    private String ownerContactInfo;
    private String ownerAvatar;
    private Boolean isPurchased;
}
