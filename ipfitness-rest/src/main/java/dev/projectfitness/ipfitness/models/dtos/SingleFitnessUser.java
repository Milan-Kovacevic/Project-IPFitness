package dev.projectfitness.ipfitness.models.dtos;

import lombok.Data;

@Data
public class SingleFitnessUser {
    private String firstName;
    private String lastName;
    private String city;
    private String username;
    private String mail;
    private String biography;
    private String contactInfo;
    private String avatar;
}
