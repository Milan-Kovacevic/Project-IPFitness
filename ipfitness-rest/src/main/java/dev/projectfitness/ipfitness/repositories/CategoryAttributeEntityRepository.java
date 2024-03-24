package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.CategoryAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryAttributeEntityRepository extends JpaRepository<CategoryAttributeEntity, Integer> {
    List<CategoryAttributeEntity> findAllByCategoryId(Integer categoryId);
    boolean existsByAttributeIdAndCategoryId(Integer attributeId, Integer categoryId);
}
