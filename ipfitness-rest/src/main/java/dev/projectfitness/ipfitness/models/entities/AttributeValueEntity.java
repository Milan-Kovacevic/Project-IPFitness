package dev.projectfitness.ipfitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "attribute_value")
public class AttributeValueEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ValueId", nullable = false)
    private Integer valueId;
    @Basic
    @Column(name = "ProgramId", nullable = false)
    private Integer programId;
    @Basic
    @Column(name = "Value", nullable = false, length = 128)
    private String value;
    @Basic
    @Column(name = "AttributeId", nullable = false)
    private Integer attributeId;
}
