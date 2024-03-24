package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.FitnessProgramEntity;
import dev.projectfitness.ipfitness.models.entities.ProgramPurchaseEntity;
import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramPurchaseEntityRepository extends JpaRepository<ProgramPurchaseEntity, Integer> {
    Integer countAllByFitnessProgramProgramId(Integer programId);

    boolean existsByFitnessProgramProgramIdAndUserId(Integer programId, Integer userId);

    List<ProgramPurchaseEntity> findAllByUserId(Integer userId);

    @Query("SELECT p.fitnessProgram FROM ProgramPurchaseEntity p WHERE (p.userId= :userId) AND (:categoryFilter IS NULL OR p.fitnessProgram.categoryId = :categoryFilter) AND (:locationFilter IS NULL OR p.fitnessProgram.location = :locationFilter) " +
            "and (:difficultyFilter IS NULL OR p.fitnessProgram.difficulty = :difficultyFilter) AND p.fitnessProgram.name LIKE %:searchQuery% AND p.fitnessProgram.deleted=false")
    Page<FitnessProgramEntity> findAllPurchasedProgramsFiltered(Integer userId, Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);

    @Query("SELECT p.fitnessProgram FROM ProgramPurchaseEntity p WHERE (p.userId= :userId) AND (p.fitnessProgram.startDate <= current_date AND p.fitnessProgram.endDate > current_date) AND (:categoryFilter IS NULL OR p.fitnessProgram.categoryId = :categoryFilter)" +
            " AND (:locationFilter IS NULL OR p.fitnessProgram.location = :locationFilter) " +
            "and (:difficultyFilter IS NULL OR p.fitnessProgram.difficulty = :difficultyFilter) AND p.fitnessProgram.name LIKE %:searchQuery% AND p.fitnessProgram.deleted=false")
    Page<FitnessProgramEntity> findAllPurchasedProgramsFilteredAndActive(Integer userId, Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);

    @Query("SELECT p.fitnessProgram FROM ProgramPurchaseEntity p WHERE (p.userId= :userId) AND (p.fitnessProgram.endDate < current_date) AND (:categoryFilter IS NULL OR p.fitnessProgram.categoryId = :categoryFilter)" +
            " AND (:locationFilter IS NULL OR p.fitnessProgram.location = :locationFilter) " +
            "and (:difficultyFilter IS NULL OR p.fitnessProgram.difficulty = :difficultyFilter) AND p.fitnessProgram.name LIKE %:searchQuery% AND p.fitnessProgram.deleted=false")
    Page<FitnessProgramEntity> findAllPurchasedProgramsFilteredAndFinished(Integer userId, Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);

    @Query("SELECT p.fitnessProgram FROM ProgramPurchaseEntity p WHERE (p.userId= :userId) AND (p.fitnessProgram.startDate > current_date) AND (:categoryFilter IS NULL OR p.fitnessProgram.categoryId = :categoryFilter)" +
            " AND (:locationFilter IS NULL OR p.fitnessProgram.location = :locationFilter) " +
            "and (:difficultyFilter IS NULL OR p.fitnessProgram.difficulty = :difficultyFilter) AND p.fitnessProgram.name LIKE %:searchQuery% AND p.fitnessProgram.deleted=false")
    Page<FitnessProgramEntity> findAllPurchasedProgramsFilteredAndUpcoming(Integer userId, Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);
}
