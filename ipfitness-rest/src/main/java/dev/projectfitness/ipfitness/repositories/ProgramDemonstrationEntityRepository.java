package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.ProgramDemonstrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramDemonstrationEntityRepository extends JpaRepository<ProgramDemonstrationEntity, Integer> {
    List<ProgramDemonstrationEntity> findAllByProgramIdOrderByOrderNumberAsc(Integer programId);
    void deleteAllByProgramId(Integer programId);
}
