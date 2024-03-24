package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.FitnessProgramEntity;
import dev.projectfitness.ipfitness.models.entities.FitnessUserEntity;
import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FitnessUserEntityRepository extends JpaRepository<FitnessUserEntity, Integer> {

    Optional<FitnessUserEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
