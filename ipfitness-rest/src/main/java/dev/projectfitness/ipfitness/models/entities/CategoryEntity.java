package dev.projectfitness.ipfitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "category")
public class CategoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CategoryId", nullable = false)
    private Integer categoryId;
    @Basic
    @Column(name = "Name", nullable = false, length = 128)
    private String name;
    @ManyToMany(mappedBy = "subscribedCategories", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<FitnessUserEntity> fitnessUsers;
}
