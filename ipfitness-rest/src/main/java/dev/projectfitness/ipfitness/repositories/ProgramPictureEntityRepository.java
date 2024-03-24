package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.ProgramPictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramPictureEntityRepository extends JpaRepository<ProgramPictureEntity, Integer> {
    List<ProgramPictureEntity> findByProgramIdOrderByOrderNumberAsc(Integer programId);
    void deleteAllByProgramId(Integer programId);
}
