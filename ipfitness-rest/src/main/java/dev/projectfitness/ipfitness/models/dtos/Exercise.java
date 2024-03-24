package dev.projectfitness.ipfitness.models.dtos;

import lombok.Data;

@Data
public class Exercise {
    private String name;
    private String type;
    private String instructions;
    private String difficulty;
}
