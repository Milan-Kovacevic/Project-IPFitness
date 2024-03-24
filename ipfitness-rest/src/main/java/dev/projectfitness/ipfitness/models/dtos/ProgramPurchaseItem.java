package dev.projectfitness.ipfitness.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.projectfitness.ipfitness.models.enums.PaymentType;
import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import lombok.Data;

import java.util.Date;

import static dev.projectfitness.ipfitness.util.Constants.READONLY_DATE_TIME_FORMAT;

@Data
public class ProgramPurchaseItem {
    private Integer purchaseId;
    private PaymentType paymentType;
    private Double price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = READONLY_DATE_TIME_FORMAT)
    private Date purchaseDate;
    private Integer programId;
    private String name;
    private String ownerFirstName;
    private String ownerLastName;
    private ProgramDifficulty difficulty;
    private TrainingLocation location;
    private String programPicture;
}
