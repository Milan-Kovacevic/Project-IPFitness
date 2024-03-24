package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionEntityRepository extends JpaRepository<QuestionEntity, Integer> {
}
