package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenEntityRepository extends JpaRepository<TokenEntity, String> {
}
