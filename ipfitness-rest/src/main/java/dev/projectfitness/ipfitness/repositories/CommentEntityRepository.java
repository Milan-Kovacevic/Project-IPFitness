package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> getAllByProgramId(Integer programId);

    Integer countAllByProgramId(Integer programId);
}
