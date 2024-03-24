package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.UserActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionEntityRepository extends JpaRepository<UserActionEntity, Integer> {
}
