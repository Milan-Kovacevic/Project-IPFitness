package dev.projectfitness.ipfitness.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FitnessUserInfoRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String city;
    @Email
    private String mail;
    private String biography;
    private String contactInfo;
}
