package dev.projectfitness.ipfitness.models.dtos;

import lombok.Data;

@Data
public class ChartData {
    Integer day;
    Double avgResult;
    Double avgPercentageCompleted;
    Double avgTrainingDuration;
}
