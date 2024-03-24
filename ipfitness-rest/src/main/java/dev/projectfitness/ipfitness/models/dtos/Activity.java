package dev.projectfitness.ipfitness.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

import static dev.projectfitness.ipfitness.util.Constants.READONLY_DATE_TIME_FORMAT;

@Data
public class Activity {
    private Integer activityId;
    private String activityType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = READONLY_DATE_TIME_FORMAT)
    private Date datePosted;
    private Integer trainingDuration;
    private Integer percentageCompleted;
    private Double result;
    private String summary;
}
