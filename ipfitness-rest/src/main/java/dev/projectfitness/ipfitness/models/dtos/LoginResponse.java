package dev.projectfitness.ipfitness.models.dtos;

import lombok.Data;

@Data
public class LoginResponse {
    private Integer userId;
    private String username;
    private String firstName;
    private String lastName;
    private String avatar;
    private Boolean active;
    private String token;
}
