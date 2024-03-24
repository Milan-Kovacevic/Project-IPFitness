package dev.projectfitness.ipfitness.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyExercises {
    private Date forDate;
    private List<Exercise> exercises;
}
