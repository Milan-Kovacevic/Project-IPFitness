package dev.projectfitness.ipfitness.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifiedUser {
    private Integer userId;
    private String verifyToken;
}
