package dev.projectfitness.ipfitness.models.dtos;

import lombok.Data;

@Data
public class AttributeValue {
    private Integer valueId;
    private Integer attributeId;
    private String value;
}
