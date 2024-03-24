package dev.projectfitness.ipfitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "category_attribute")
public class CategoryAttributeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "AttributeId", nullable = false)
    private Integer attributeId;
    @Basic
    @Column(name = "Name", nullable = false, length = 128)
    private String name;
    @Basic
    @Column(name = "CategoryId", nullable = false)
    private Integer categoryId;

}
