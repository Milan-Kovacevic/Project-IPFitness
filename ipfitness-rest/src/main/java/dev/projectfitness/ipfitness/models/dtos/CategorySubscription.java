package dev.projectfitness.ipfitness.models.dtos;

import lombok.Data;

@Data
public class CategorySubscription {
    private Integer categoryId;
    private String categoryName;
    private Boolean isSubscribed;
}
