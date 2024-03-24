package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.AttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttributeValueEntityRepository extends JpaRepository<AttributeValueEntity, Integer> {
    List<AttributeValueEntity> findAllByProgramId(Integer programId);

    Optional<AttributeValueEntity> findByProgramIdAndAttributeId(Integer programId, Integer attributeId);

    void deleteAllByProgramId(Integer programId);
}
